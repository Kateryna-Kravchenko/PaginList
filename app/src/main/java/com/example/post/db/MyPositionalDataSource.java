package com.example.post.db;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;
import com.example.post.db.Post;
import com.example.post.db.PostDao;

import java.util.List;

public class MyPositionalDataSource extends PositionalDataSource<Post> {

    private PostDao store;
    private Integer lastKnowPosition;

    public MyPositionalDataSource(PostDao postStorage) {
        this.store = postStorage;
        this.lastKnowPosition = postStorage.getLastIndex();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Post> callback) {
        List<Post> result = store.allPostByPosition(lastKnowPosition - params.requestedStartPosition,
                params.requestedLoadSize);
        callback.onResult(result, 0);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Post> callback) {
        List<Post> result = store.allPostByPosition(lastKnowPosition - params.startPosition,
                params.loadSize);
        callback.onResult(result);
    }
}