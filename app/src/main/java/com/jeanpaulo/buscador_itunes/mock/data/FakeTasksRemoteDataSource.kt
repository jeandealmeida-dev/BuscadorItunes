/*
 * Copyright (C) 2019 The Android Open Source Project
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
package com.jeanpaulo.buscador_itunes.mock.data

import androidx.lifecycle.LiveData
import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.datasource.MusicDataSource
import com.jeanpaulo.buscador_itunes.datasource.remote.util.ItunesResponse
import com.jeanpaulo.buscador_itunes.datasource.remote.util.ItunesResponse2
import java.util.LinkedHashMap

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
object FakeTasksRemoteDataSource :
    MusicDataSource {

    private var MUSICS_SERVICE_DATA: LinkedHashMap<String, Music> = LinkedHashMap()
    override suspend fun searchMusic(
        term: String,
        mediaType: String,
        offset: Int,
        limit: Int
    ): Result<ItunesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCollection(term: Long, mediaType: String): ItunesResponse2 {
        TODO("Not yet implemented")
    }

    override fun observeMusics(): LiveData<Result<List<Music>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMusics(): Result<List<Music>> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshMusics() {
        TODO("Not yet implemented")
    }

    override fun observeMusic(musicId: Long): LiveData<Result<Music>>? {
        /*return observableMusics.map { musics: Result<List<Music>> ->
            when (musics) {
                is Result.Loading -> Result.Loading
                is Error -> Error(musics.)
                is Result.Success -> {
                    val task = musics.data.firstOrNull() { it.id == musicId }
                        ?: return@map Error(Exception("Not found"))
                    Result.Success(musics.data)
                }
            }
        }*/
        return null
    }

    override suspend fun getMusic(musicId: Long): Result<Music> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshMusic(musicId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun saveMusic(music: Music) {
        TODO("Not yet implemented")
    }

    override suspend fun completeMusic(music: Music) {
        TODO("Not yet implemented")
    }

    override suspend fun completeMusic(musicId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun activateMusic(music: Music) {
        TODO("Not yet implemented")
    }

    override suspend fun activateMusic(musicId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedMusics() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllMusics() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMusic(musicId: Long) {
        TODO("Not yet implemented")
    }

}
/*
private var TASKS_SERVICE_DATA: LinkedHashMap<String, Music> = LinkedHashMap()

private val observableTasks = MutableLiveData<Result<List<Music>>>()

override suspend fun refreshTasks() {
    observableTasks.postValue(getTasks())
}

override suspend fun refreshTask(taskId: String) {
    refreshTasks()
}

override fun observeTasks(): LiveData<Result<List<Music>>> {
    return observableTasks
}

override fun observeTask(taskId: String): LiveData<Result<Music>> {
    return observableTasks.map { tasks ->
        when (tasks) {
            is Result.Loading -> Result.Loading
            is Error -> Error(tasks.exception)
            is Result.Success -> {
                val task = tasks.data.firstOrNull() { it.id == taskId }
                    ?: return@map Error(Exception("Not found"))
                Success(task)
            }
        }
    }
}

override suspend fun getTask(taskId: String): Result<Task> {
    TASKS_SERVICE_DATA[taskId]?.let {
        return Result.Success(it)
    }
    return Error(Exception("Could not find task"))
}

override suspend fun getTasks(): Result<List<Task>> {
    return Result.Success(TASKS_SERVICE_DATA.values.toList())
}

override suspend fun saveTask(task: Task) {
    TASKS_SERVICE_DATA[task.id] = task
}

override suspend fun completeTask(task: Task) {
    val completedTask = Task(task.title, task.description, true, task.id)
    TASKS_SERVICE_DATA[task.id] = completedTask
}

override suspend fun completeTask(taskId: String) {
    // Not required for the remote data source.
}

override suspend fun activateTask(task: Task) {
    val activeTask = Task(task.title, task.description, false, task.id)
    TASKS_SERVICE_DATA[task.id] = activeTask
}

override suspend fun activateTask(taskId: String) {
    // Not required for the remote data source.
}

override suspend fun clearCompletedTasks() {
    TASKS_SERVICE_DATA = TASKS_SERVICE_DATA.filterValues {
        !it.isCompleted
    } as LinkedHashMap<String, Task>
}

override suspend fun deleteTask(taskId: String) {
    TASKS_SERVICE_DATA.remove(taskId)
    refreshTasks()
}

override suspend fun deleteAllTasks() {
    TASKS_SERVICE_DATA.clear()
    refreshTasks()
}
}*/
