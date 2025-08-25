package com.example.github_user_search_app.data.model

import com.google.gson.annotations.SerializedName

data class Repository(
    val id: Long,
    val name: String,
    val description: String?,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("clone_url") val cloneUrl: String,
    @SerializedName("git_url") val gitUrl: String,
    @SerializedName("ssh_url") val sshUrl: String,
    @SerializedName("svn_url") val svnUrl: String,
    val language: String?,
    @SerializedName("default_branch") val defaultBranch: String,
    val fork: Boolean,
    val archived: Boolean,
    val disabled: Boolean,
    @SerializedName("open_issues_count") val openIssuesCount: Int,
    @SerializedName("forks_count") val forksCount: Int,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("watchers_count") val watchersCount: Int,
    @SerializedName("size") val sizeInKB: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("pushed_at") val pushedAt: String,
    val private: Boolean,
    @SerializedName("has_issues") val hasIssues: Boolean,
    @SerializedName("has_projects") val hasProjects: Boolean,
    @SerializedName("has_downloads") val hasDownloads: Boolean,
    @SerializedName("has_wiki") val hasWiki: Boolean,
    @SerializedName("has_pages") val hasPages: Boolean,
    @SerializedName("has_discussions") val hasDiscussions: Boolean,
    @SerializedName("allow_forking") val allowForking: Boolean,
    @SerializedName("allow_squash_merge") val allowSquashMerge: Boolean,
    @SerializedName("allow_merge_commit") val allowMergeCommit: Boolean,
    @SerializedName("allow_rebase_merge") val allowRebaseMerge: Boolean,
    @SerializedName("allow_auto_merge") val allowAutoMerge: Boolean,
    @SerializedName("delete_branch_on_merge") val deleteBranchOnMerge: Boolean,
    @SerializedName("allow_update_branch") val allowUpdateBranch: Boolean,
    @SerializedName("use_squash_pr_title_as_default") val useSquashPrTitleAsDefault: Boolean,
    @SerializedName("squash_merge_commit_message") val squashMergeCommitMessage: String?,
    @SerializedName("squash_merge_commit_title") val squashMergeCommitTitle: String?,
    @SerializedName("merge_commit_message") val mergeCommitMessage: String?,
    @SerializedName("merge_commit_title") val mergeCommitTitle: String?,
    val topics: List<String>?,
    val visibility: String,
    val license: License?,
    val owner: RepositoryOwner
)

data class License(
    val key: String,
    val name: String,
    val url: String?,
    @SerializedName("spdx_id") val spdxId: String?,
    @SerializedName("node_id") val nodeId: String
)

data class RepositoryOwner(
    val login: String,
    val id: Long,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("html_url") val htmlUrl: String,
    val type: String
)
