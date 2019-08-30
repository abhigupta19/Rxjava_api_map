package com.sar.user.rxjava_api_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getObservable().observeOn(AndroidSchedulers.mainThread()).map(new Function<User, User>() {
            @Override
            public User apply(User user) throws Exception {
                user.setName(user.getName().toUpperCase());
                user.setEmail(user.getName()+"@gmail.com");
                return user;
            }
        }).subscribe(new Observer<User>() {
            /**
             * Provides the Observer with the means of cancelling (disposing) the
             * connection (channel) with the Observable in both
             * synchronous (from within {@link #onNext(Object)}) and asynchronous manner.
             *
             * @param d the Disposable instance whose {@link Disposable#dispose()} can
             *          be called anytime to cancel the connection
             * @since 2.0
             */
            @Override
            public void onSubscribe(Disposable d) {
                 disposable=d;
            }

            /**
             * Provides the Observer with a new item to observe.
             * <p>
             * The {@link Observable} may call this method 0 or more times.
             * <p>
             * The {@code Observable} will not call this method again after it calls either {@link #onComplete} or
             * {@link #onError}.
             *
             * @param user the item emitted by the Observable
             */
            @Override
            public void onNext(User user) {
                Log.i("hiiii",user.getEmail());

            }

            /**
             * Notifies the Observer that the {@link Observable} has experienced an error condition.
             * <p>
             * If the {@link Observable} calls this method, it will not thereafter call {@link #onNext} or
             * {@link #onComplete}.
             *
             * @param e the exception encountered by the Observable
             */
            @Override
            public void onError(Throwable e) {

            }

            /**
             * Notifies the Observer that the {@link Observable} has finished sending push-based notifications.
             * <p>
             * The {@link Observable} will not call this method if it calls {@link #onError}.
             */
            @Override
            public void onComplete() {

            }
        });

    }

    private Observable<User> getObservable() {

        String [] names=new String[]{"abhinav","anurag","don"};
        final List<User> users=new ArrayList<>();

        for (String s:names)
        {
            User user=new User();
            user.setName(s);
            user.setGender("Male");
            users.add(user);
        }

        return  Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                for(User user:users)
                {
                    if(disposable.isDisposed())
                    {
                        return;
                    }

                    emitter.onNext(user);
                }
               if(!disposable.isDisposed())
               {
                   emitter.onComplete();
               }



            }
        }).subscribeOn(Schedulers.io());
    }
}
