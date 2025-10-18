package com.raf.mobiletaskcodeidtest.auth.data.repository

import android.util.Log
import com.couchbase.lite.DataSource
import com.couchbase.lite.Database
import com.couchbase.lite.Expression
import com.couchbase.lite.MutableDocument
import com.couchbase.lite.QueryBuilder
import com.couchbase.lite.SelectResult
import com.raf.mobiletaskcodeidtest.auth.data.local.AuthDataStore
import com.raf.mobiletaskcodeidtest.auth.data.repository.mapper.UserMapper.toUserDocument
import com.raf.mobiletaskcodeidtest.auth.domain.model.User
import com.raf.mobiletaskcodeidtest.auth.domain.repository.AuthRepository
import com.raf.mobiletaskcodeidtest.core.data.utils.SessionEncryptionManager
import com.raf.mobiletaskcodeidtest.core.domain.contract.AuthTokenProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val database: Database,
) : AuthTokenProvider, AuthRepository {

    override suspend fun getAccountInfo(userId: String): String? {
        try {
            val collection = database.getCollection(COLLECTION) ?: return null

            val query = QueryBuilder
                .select(SelectResult.property("email"))
                .from(DataSource.collection(collection))
                .where(Expression.property("id").equalTo(Expression.string(userId)))

            val results = query.execute().allResults()
            Log.d(TAG, "Account Info: $results")
            if (results.isEmpty()) {
                Log.w(TAG, "No Account Info for userId: $userId")
                return null
            } else {
                return results.first()?.getString("email")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get Account Info", e)
            return null
        }
    }

    override suspend fun saveSessionToken(token: String) {
        try {
            val encryptedToken = SessionEncryptionManager.encrypt(token)
            if (encryptedToken == null) {
                Log.e(TAG, "saveSessionToken: Failed to encrypt token")
                return
            }
            authDataStore.saveSessionToken(encryptedToken)
        } catch (e: Exception) {
            Log.e(TAG, "saveSessionToken: ${e.message}", e)
        }
    }

    override suspend fun loginUser(
        email: String,
        password: String,
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val collection = database.getCollection(COLLECTION)
                ?: return@withContext Result.failure(Exception("User not found"))

            val query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.collection(collection))
                .where(
                    Expression.property("email").equalTo(Expression.string(email))
                        .and(Expression.property("type").equalTo(Expression.string(DOCUMENT_TYPE)))
                )
            val results = query.execute().allResults()
            if (results.isEmpty()) {
                Log.w(TAG, "Login: No user found with email: $email")
                return@withContext Result.failure(Exception("Invalid email or password"))
            }

            val documentDict = results.first().getDictionary(0)
            val storedEncryptedPassword = documentDict?.getString("encryptedPassword")
            val userId = documentDict?.getString("id")

            if (storedEncryptedPassword == null || userId == null) {
                Log.w(TAG, "Login: User not found")
                return@withContext Result.failure(Exception("Invalid email or password"))
            }

            // Simulate password validation
            val decryptedPassword = SessionEncryptionManager.decrypt(storedEncryptedPassword)
            if (decryptedPassword != password) {
                Log.w(TAG, "Login: Invalid password")
                return@withContext Result.failure(Exception("Invalid email or password"))
            }

            return@withContext Result.success(userId)
        } catch (e: Exception) {
            Log.e(TAG, "loginUser: ${e.message}", e)
            return@withContext Result.failure(e)
        }
    }

    override suspend fun registerUser(user: User): Result<User> = withContext(Dispatchers.IO) {
        val collection = database.getCollection(COLLECTION)
            ?: database.createCollection(COLLECTION)

        val userId = UUID.randomUUID().toString()
        val encryptedPassword =
            SessionEncryptionManager.encrypt(user.password) ?: return@withContext Result.failure(
                Exception("Failed to encrypt password")
            )
        val userDocumentData = user.copy(userId = userId).toUserDocument(encryptedPassword)
        val document = MutableDocument(userDocumentData.id).apply {
            setString("id", userDocumentData.id)
            setString("email", userDocumentData.email)
            setString("encryptedPassword", userDocumentData.encryptedPassword)
            setString("type", userDocumentData.type)
        }

        try {
            collection.save(document)
            Log.d(TAG, "registerUser: User registered successfully with ID: $userId")
        } catch (e: Exception) {
            Log.e(TAG, "registerUser: ${e.message}", e)
            return@withContext Result.failure(e)
        }
        return@withContext Result.success(user.copy(userId = userId))
    }

    override fun getAuthToken(): Flow<String?> {
        return authDataStore.getSessionToken().map { encryptedToken ->
            encryptedToken?.let { SessionEncryptionManager.decrypt(it) }
        }
    }

    override suspend fun logout() {
        try {
            authDataStore.clearSessionToken()
        } catch (e: Exception) {
            Log.e(TAG, "logout: ${e.message}", e)
        }
    }

    private companion object {
        private const val TAG = "AuthRepositoryImpl"
        private const val DOCUMENT_TYPE = "user"
        private const val COLLECTION = "user_collection"
    }
}