package com.jeanpaulo.buscador_itunes.datasource.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jeanpaulo.buscador_itunes.datasource.local.entity.MusicEntity
import com.jeanpaulo.buscador_itunes.model.Music

@Dao
interface MusicDao {


    /**
     * Observes list of tasks.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM Music")
    fun observeMusics(): LiveData<List<MusicEntity>>

    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM Music")
    suspend fun getMusics(): List<MusicEntity>

    /**
     * Select a task by id.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM Music WHERE trackId = :musicId")
    suspend fun getMusicById(musicId: Long): MusicEntity?

    /**
     * Observes a single task.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM Music WHERE trackId = :taskId")
    fun observeMusicById(taskId: Long): LiveData<MusicEntity>

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param task the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(music: MusicEntity)

    /**
     * Update a task.
     *
     * @param task task to be updated
     * @return the number of tasks updated. This should always be 1.
     */
    @Update
    suspend fun updateMusic(music: MusicEntity): Int

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
    @Query("DELETE FROM Music WHERE trackId = :taskId")
    suspend fun deleteMusicById(taskId: Long): Int

    /**
     * Delete all tasks.
     */
    @Query("DELETE FROM Music")
    suspend fun deleteMusics()

    /**
     * Delete all completed tasks from the table.
     *
     * @return the number of tasks deleted.

    @Query("DELETE FROM Music WHERE completed = 1")
    suspend fun deleteCompletedMusics(): Int
     */

}