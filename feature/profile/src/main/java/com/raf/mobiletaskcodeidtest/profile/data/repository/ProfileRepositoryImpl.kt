package com.raf.mobiletaskcodeidtest.profile.data.repository

import android.util.Log
import com.couchbase.lite.DataSource
import com.couchbase.lite.Database
import com.couchbase.lite.Expression
import com.couchbase.lite.MutableDocument
import com.couchbase.lite.QueryBuilder
import com.couchbase.lite.SelectResult
import com.raf.mobiletaskcodeidtest.core.domain.contract.ProfileProvider
import com.raf.mobiletaskcodeidtest.profile.data.repository.mapper.ProfileMapper.toProfileDocument
import com.raf.mobiletaskcodeidtest.profile.domain.model.Profile
import com.raf.mobiletaskcodeidtest.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val database: Database,
) : ProfileRepository, ProfileProvider {

    override suspend fun isUserAvailable(userId: String?): Boolean {
        val collection = database.getCollection(COLLECTION)
        if (userId == null || collection == null) {
            return false
        }

        val query = QueryBuilder
            .select(SelectResult.property("user_id"))
            .from(DataSource.collection(collection))
            .where(Expression.property("user_id").equalTo(Expression.string(userId)))

        val results = query.execute().allResults()
        Log.d(TAG, "getIsUserAvailable: $results")
        return results.isNotEmpty()
    }

    override suspend fun getProfile(userId: String): Result<Profile> = withContext(Dispatchers.IO) {
        val collection = database.getCollection(COLLECTION)
            ?: return@withContext Result.failure(Exception("User not found"))
        val query = QueryBuilder
            .select(SelectResult.all())
            .from(DataSource.collection(collection))
            .where(
                Expression.property("user_id").equalTo(Expression.string(userId))
            )
        val results = query.execute().allResults()
        if (results.isEmpty()) {
            Log.w(TAG, "Profile: No user found with id: $userId")
            return@withContext Result.failure(Exception("User not found"))
        } else {
            val documentDict = results.first().getDictionary(0)
            val profile = Profile(
                userId = documentDict?.getString("user_id") ?: "",
                email = "", // In Account
                name = documentDict?.getString("name") ?: "",
                picturePath = documentDict?.getString("picture_path") ?: ""
            )
            return@withContext Result.success(profile)
        }
    }

    override suspend fun updateProfile(profile: Profile): Result<Profile> =
        withContext(Dispatchers.IO) {
            val collection = database.getCollection(COLLECTION)
                ?: database.createCollection(COLLECTION)

            val profileDocumentData = profile.toProfileDocument()
            val document = MutableDocument(profileDocumentData.userId).apply {
                setString("user_id", profileDocumentData.userId)
                setString("name", profileDocumentData.name)
                setString("picture_path", profileDocumentData.picturePath)
                setString("type", profileDocumentData.type)
            }

            try {
                collection.save(document)
                Log.d(
                    TAG,
                    "registerUser: User registered successfully with ID: ${profileDocumentData.userId}"
                )
            } catch (e: Exception) {
                Log.e(TAG, "registerUser: ${e.message}", e)
                return@withContext Result.failure(e)
            }
            return@withContext Result.success(profile)
        }

    private companion object {
        private const val TAG = "ProfileRepositoryImpl"
        private const val COLLECTION = "user_profile_collection"
    }
}