package com.jeanpaulo.buscador_itunes.datasource.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.Playlist

@Dao
interface PlaylistDao {


    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Transaction
    @Query("SELECT * FROM Playlist")
    suspend fun getPlaylists(): List<Playlist>

    /**
     * Select a task by id.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId = :playlistId")
    suspend fun getPlaylistById(playlistId: String): Playlist?


    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param task the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist)

    /**
     * Update a task.
     *
     * @param task task to be updated
     * @return the number of tasks updated. This should always be 1.
     */
    @Update
    suspend fun updatePlaylist(playlist: Playlist): Int

    /**
     * Update the complete status of a task
     *
     * @param taskId id of the task
     * @param completed status to be updated

    @Query("UPDATE Music SET completed = :completed WHERE entryid = :taskId")
    suspend fun updateCompleted(taskId: String, completed: Boolean)
     */

    /**
     * Delete a task by id.
     *
     * @return the number of tasks deleted. This should always be 1.
     */
    @Query("DELETE FROM Playlist WHERE playlistId = :playlistId")
    suspend fun deletePlaylistById(playlistId: Long): Int

    /**
     * Delete all tasks.
     */
    @Query("DELETE FROM Playlist")
    suspend fun deletePlaylists()

    /**
     * Delete all completed tasks from the table.
     *
     * @return the number of tasks deleted.

    @Query("DELETE FROM Music WHERE completed = 1")
    suspend fun deleteCompletedMusics(): Int
     */

}