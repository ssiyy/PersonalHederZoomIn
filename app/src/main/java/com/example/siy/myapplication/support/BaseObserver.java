package com.example.siy.myapplication.support;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * RxJava的基础观察者类,如果你不想用Disposable取消观察，可以用这个类，它会在完成任务或者出错是自动取消观察
 * <p>
 * Created by Siy on 2018/10/22.
 *
 * @author Siy
 */
public class BaseObserver<T> implements Observer<T> {
    private Disposable disposable;

    protected void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(T bean) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {
        dispose();
    }
}
