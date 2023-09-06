/*
 * Copyright 2023 Blogger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.blogger.data


import com.peterchege.blogger.core.api.requests.FollowUser
import com.peterchege.blogger.core.api.requests.LikePost
import com.peterchege.blogger.core.api.requests.Viewer
import com.peterchege.blogger.core.api.responses.*
import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.room.entities.PostRecordWithCommentsLikesViews
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.data.local.posts.cached_posts.CachedPostsDataSource
import com.peterchege.blogger.data.local.posts.saved_posts.SavedPostsDataSource
import com.peterchege.blogger.data.remote.posts.RemotePostsDataSource
import com.peterchege.blogger.domain.models.PostUI
import com.peterchege.blogger.domain.mappers.toDomain
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.PostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val cachedPostsDataSource: CachedPostsDataSource,
    private val remotePostsDataSource: RemotePostsDataSource,
    private val savedPostsDataSource: SavedPostsDataSource,
    private val authRepository:AuthRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
):PostRepository {

    override fun getAllPosts():Flow<List<PostUI>> {
        val cachedPosts = cachedPostsDataSource.getCachedPosts()
        val savedPostIds = savedPostsDataSource.getSavedPostIds()
        val authUser = authRepository.getLoggedInUser()
        val isUserLoggedIn = authRepository.isUserLoggedIn
        return combine(cachedPosts,savedPostIds,authUser,isUserLoggedIn){ posts, ids,user,loggedIn ->
            posts.map { post ->
                post.toDomain(
                    isLiked = if (loggedIn) post.likes.map { it.userId }.contains(user?._id) else false,
                    isSaved = ids.contains(post._id),
                    isProfile = false
                )
            }
        }
    }

    override suspend fun uploadPost(body: RequestBody): NetworkResult<UploadPostResponse> {
        return remotePostsDataSource.uploadPost(body= body)
    }

    override fun getPostById(postId: String): Flow<Post?> = flow {
        val cachedPost = cachedPostsDataSource.getCachedPostById(postId).first()
        val savedPost = savedPostsDataSource.getPostFromRoom(postId).first()
        if (cachedPost == null && savedPost == null){
            val response = remotePostsDataSource.getPostById(postId = postId)
            when(response){
                is NetworkResult.Success -> {
                    val remotePost = response.data.post
                    emit(remotePost)
                    if (remotePost != null){
                        cachedPostsDataSource.insertCachedPosts(listOf(remotePost))
                    }
                }
                is NetworkResult.Error -> {
                    emit(null)
                }
                is NetworkResult.Exception -> {
                    emit(null)
                }
            }

        }else if(cachedPost == null){
            emit(savedPost)

        }else{
            emit(cachedPost)
        }
    }

    override fun getSavedPostIds(): Flow<List<String>> {
        return savedPostsDataSource.getSavedPostIds()

    }

    override suspend fun deletePostFromApi(postId: String): NetworkResult<DeleteResponse> {
        return remotePostsDataSource.deletePostFromApi(postId = postId)
    }

    override suspend fun addView(viewer: Viewer): NetworkResult<ViewResponse> {
        return remotePostsDataSource.addView(viewer = viewer)
    }

    override suspend fun likePost(likePost: LikePost): NetworkResult<LikeResponse> {
        return remotePostsDataSource.likePost(likePost = likePost)
    }

    override suspend fun unlikePost(likePost: LikePost): NetworkResult<LikeResponse> {
        return remotePostsDataSource.unlikePost(likePost = likePost)
    }

    override suspend fun followUser(followUser: FollowUser): NetworkResult<FollowResponse> {
        return remotePostsDataSource.followUser(followUser = followUser)
    }

    override suspend fun unfollowUser(followUser: FollowUser): NetworkResult<FollowResponse> {
        return remotePostsDataSource.unfollowUser(followUser = followUser)
    }

    override suspend fun searchPosts(searchTerm: String): NetworkResult<SearchPostResponse> {
        return remotePostsDataSource.searchPosts(searchTerm = searchTerm)
    }

    override suspend fun insertSavedPost(post: Post) = withContext(ioDispatcher) {
        savedPostsDataSource.insertPost(post = post)
    }

    override suspend fun deleteAllSavedPosts() {
        withContext(ioDispatcher){
            savedPostsDataSource.deleteAllPosts()
        }
    }

    override suspend fun deleteSavedPostById(id: String) {
        withContext(ioDispatcher){
            savedPostsDataSource.deletePostById(id = id)
        }
    }

    override suspend fun getSavedPost(postId: String): Flow<Post?> {
        return savedPostsDataSource.getPostFromRoom(postId = postId)

    }

    override fun getAllSavedPosts(): Flow<List<PostRecordWithCommentsLikesViews>> {
        return savedPostsDataSource.getAllPostsFromRoom()
    }


}