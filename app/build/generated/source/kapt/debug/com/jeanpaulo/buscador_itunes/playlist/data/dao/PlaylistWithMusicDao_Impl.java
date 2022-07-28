package com.jeanpaulo.buscador_itunes.playlist.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.jeanpaulo.buscador_itunes.music.data.local.model.MusicEntity;
import com.jeanpaulo.buscador_itunes.playlist.data.PlaylistMusicJoin;
import com.jeanpaulo.buscador_itunes.util.DateConverter;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlaylistWithMusicDao_Impl implements PlaylistWithMusicDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlaylistMusicJoin> __insertionAdapterOfPlaylistMusicJoin;

  private final SharedSQLiteStatement __preparedStmtOfRemoveMusicFromPlaylist;

  private final DateConverter __dateConverter = new DateConverter();

  public PlaylistWithMusicDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlaylistMusicJoin = new EntityInsertionAdapter<PlaylistMusicJoin>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `play_music_join` (`playlistId`,`musicId`,`playlistMusicJoinId`) VALUES (?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PlaylistMusicJoin value) {
        stmt.bindLong(1, value.getPlaylistId());
        stmt.bindLong(2, value.getMusicId());
        stmt.bindLong(3, value.getId());
      }
    };
    this.__preparedStmtOfRemoveMusicFromPlaylist = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM play_music_join WHERE musicId=? AND playlistId=?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final PlaylistMusicJoin playlistMusicJoin,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfPlaylistMusicJoin.insertAndReturnId(playlistMusicJoin);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object removeMusicFromPlaylist(final long musicId, final long playlistId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveMusicFromPlaylist.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, musicId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, playlistId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfRemoveMusicFromPlaylist.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object getPlaylistsWithThisMusic(final long musicId,
      final Continuation<? super List<MusicEntity>> continuation) {
    final String _sql = "SELECT * FROM playlist INNER JOIN play_music_join ON playlist.playlistId=play_music_join.playlistId WHERE play_music_join.musicId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, musicId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MusicEntity>>() {
      @Override
      public List<MusicEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMusicId = CursorUtil.getColumnIndexOrThrow(_cursor, "musicId");
          final List<MusicEntity> _result = new ArrayList<MusicEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MusicEntity _item;
            final Long _tmpMusicId;
            if (_cursor.isNull(_cursorIndexOfMusicId)) {
              _tmpMusicId = null;
            } else {
              _tmpMusicId = _cursor.getLong(_cursorIndexOfMusicId);
            }
            _item = new MusicEntity(null,null,null,null,null,null,_tmpMusicId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getMusicsFromPlaylist(final long playlistId,
      final Continuation<? super List<MusicEntity>> continuation) {
    final String _sql = "SELECT * FROM music INNER JOIN play_music_join ON music.musicId=play_music_join.musicId WHERE play_music_join.playlistId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MusicEntity>>() {
      @Override
      public List<MusicEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfArtworkUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "artworkUrl");
          final int _cursorIndexOfReleaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "releaseDate");
          final int _cursorIndexOfIsStreamable = CursorUtil.getColumnIndexOrThrow(_cursor, "isStreamable");
          final int _cursorIndexOfTrackTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "trackTimeMillis");
          final int _cursorIndexOfPreviewUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "previewUrl");
          final int _cursorIndexOfMusicId = CursorUtil.getColumnIndexOrThrow(_cursor, "musicId");
          final int _cursorIndexOfCollectionId = CursorUtil.getColumnIndexOrThrow(_cursor, "collectionId");
          final int _cursorIndexOfArtistId = CursorUtil.getColumnIndexOrThrow(_cursor, "artistId");
          final int _cursorIndexOfMusicId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "musicId");
          final List<MusicEntity> _result = new ArrayList<MusicEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MusicEntity _item;
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpArtworkUrl;
            if (_cursor.isNull(_cursorIndexOfArtworkUrl)) {
              _tmpArtworkUrl = null;
            } else {
              _tmpArtworkUrl = _cursor.getString(_cursorIndexOfArtworkUrl);
            }
            final Date _tmpReleaseDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfReleaseDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfReleaseDate);
            }
            _tmpReleaseDate = __dateConverter.toDate(_tmp);
            final Boolean _tmpIsStreamable;
            final Integer _tmp_1;
            if (_cursor.isNull(_cursorIndexOfIsStreamable)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsStreamable);
            }
            _tmpIsStreamable = _tmp_1 == null ? null : _tmp_1 != 0;
            final Long _tmpTrackTimeMillis;
            if (_cursor.isNull(_cursorIndexOfTrackTimeMillis)) {
              _tmpTrackTimeMillis = null;
            } else {
              _tmpTrackTimeMillis = _cursor.getLong(_cursorIndexOfTrackTimeMillis);
            }
            final String _tmpPreviewUrl;
            if (_cursor.isNull(_cursorIndexOfPreviewUrl)) {
              _tmpPreviewUrl = null;
            } else {
              _tmpPreviewUrl = _cursor.getString(_cursorIndexOfPreviewUrl);
            }
            final Long _tmpMusicId;
            if (_cursor.isNull(_cursorIndexOfMusicId)) {
              _tmpMusicId = null;
            } else {
              _tmpMusicId = _cursor.getLong(_cursorIndexOfMusicId);
            }
            final Long _tmpMusicId_1;
            if (_cursor.isNull(_cursorIndexOfMusicId_1)) {
              _tmpMusicId_1 = null;
            } else {
              _tmpMusicId_1 = _cursor.getLong(_cursorIndexOfMusicId_1);
            }
            _item = new MusicEntity(_tmpName,_tmpArtworkUrl,_tmpReleaseDate,_tmpIsStreamable,_tmpTrackTimeMillis,_tmpPreviewUrl,_tmpMusicId);
            final Long _tmpCollectionId;
            if (_cursor.isNull(_cursorIndexOfCollectionId)) {
              _tmpCollectionId = null;
            } else {
              _tmpCollectionId = _cursor.getLong(_cursorIndexOfCollectionId);
            }
            _item.setCollectionId(_tmpCollectionId);
            final Long _tmpArtistId;
            if (_cursor.isNull(_cursorIndexOfArtistId)) {
              _tmpArtistId = null;
            } else {
              _tmpArtistId = _cursor.getLong(_cursorIndexOfArtistId);
            }
            _item.setArtistId(_tmpArtistId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getMusicInPlaylist(final long musicId, final long playlistId,
      final Continuation<? super MusicEntity> continuation) {
    final String _sql = "SELECT * FROM music INNER JOIN play_music_join ON music.musicId =play_music_join.musicId WHERE play_music_join.playlistId=? AND music.musicId=? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, musicId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MusicEntity>() {
      @Override
      public MusicEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfArtworkUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "artworkUrl");
          final int _cursorIndexOfReleaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "releaseDate");
          final int _cursorIndexOfIsStreamable = CursorUtil.getColumnIndexOrThrow(_cursor, "isStreamable");
          final int _cursorIndexOfTrackTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "trackTimeMillis");
          final int _cursorIndexOfPreviewUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "previewUrl");
          final int _cursorIndexOfMusicId = CursorUtil.getColumnIndexOrThrow(_cursor, "musicId");
          final int _cursorIndexOfCollectionId = CursorUtil.getColumnIndexOrThrow(_cursor, "collectionId");
          final int _cursorIndexOfArtistId = CursorUtil.getColumnIndexOrThrow(_cursor, "artistId");
          final int _cursorIndexOfMusicId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "musicId");
          final MusicEntity _result;
          if(_cursor.moveToFirst()) {
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpArtworkUrl;
            if (_cursor.isNull(_cursorIndexOfArtworkUrl)) {
              _tmpArtworkUrl = null;
            } else {
              _tmpArtworkUrl = _cursor.getString(_cursorIndexOfArtworkUrl);
            }
            final Date _tmpReleaseDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfReleaseDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfReleaseDate);
            }
            _tmpReleaseDate = __dateConverter.toDate(_tmp);
            final Boolean _tmpIsStreamable;
            final Integer _tmp_1;
            if (_cursor.isNull(_cursorIndexOfIsStreamable)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsStreamable);
            }
            _tmpIsStreamable = _tmp_1 == null ? null : _tmp_1 != 0;
            final Long _tmpTrackTimeMillis;
            if (_cursor.isNull(_cursorIndexOfTrackTimeMillis)) {
              _tmpTrackTimeMillis = null;
            } else {
              _tmpTrackTimeMillis = _cursor.getLong(_cursorIndexOfTrackTimeMillis);
            }
            final String _tmpPreviewUrl;
            if (_cursor.isNull(_cursorIndexOfPreviewUrl)) {
              _tmpPreviewUrl = null;
            } else {
              _tmpPreviewUrl = _cursor.getString(_cursorIndexOfPreviewUrl);
            }
            final Long _tmpMusicId;
            if (_cursor.isNull(_cursorIndexOfMusicId)) {
              _tmpMusicId = null;
            } else {
              _tmpMusicId = _cursor.getLong(_cursorIndexOfMusicId);
            }
            final Long _tmpMusicId_1;
            if (_cursor.isNull(_cursorIndexOfMusicId_1)) {
              _tmpMusicId_1 = null;
            } else {
              _tmpMusicId_1 = _cursor.getLong(_cursorIndexOfMusicId_1);
            }
            _result = new MusicEntity(_tmpName,_tmpArtworkUrl,_tmpReleaseDate,_tmpIsStreamable,_tmpTrackTimeMillis,_tmpPreviewUrl,_tmpMusicId);
            final Long _tmpCollectionId;
            if (_cursor.isNull(_cursorIndexOfCollectionId)) {
              _tmpCollectionId = null;
            } else {
              _tmpCollectionId = _cursor.getLong(_cursorIndexOfCollectionId);
            }
            _result.setCollectionId(_tmpCollectionId);
            final Long _tmpArtistId;
            if (_cursor.isNull(_cursorIndexOfArtistId)) {
              _tmpArtistId = null;
            } else {
              _tmpArtistId = _cursor.getLong(_cursorIndexOfArtistId);
            }
            _result.setArtistId(_tmpArtistId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getMusicOnPlaylist(final long musicId, final long playlistId,
      final Continuation<? super List<PlaylistMusicJoin>> continuation) {
    final String _sql = "SELECT * FROM play_music_join WHERE musicId=? AND playlistId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, musicId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, playlistId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PlaylistMusicJoin>>() {
      @Override
      public List<PlaylistMusicJoin> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlaylistId = CursorUtil.getColumnIndexOrThrow(_cursor, "playlistId");
          final int _cursorIndexOfMusicId = CursorUtil.getColumnIndexOrThrow(_cursor, "musicId");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "playlistMusicJoinId");
          final List<PlaylistMusicJoin> _result = new ArrayList<PlaylistMusicJoin>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final PlaylistMusicJoin _item;
            final long _tmpPlaylistId;
            _tmpPlaylistId = _cursor.getLong(_cursorIndexOfPlaylistId);
            final long _tmpMusicId;
            _tmpMusicId = _cursor.getLong(_cursorIndexOfMusicId);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new PlaylistMusicJoin(_tmpPlaylistId,_tmpMusicId,_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
