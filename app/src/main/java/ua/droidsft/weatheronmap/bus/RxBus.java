package ua.droidsft.weatheronmap.bus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Simple event bus using Rx.
 * Created by Vlad on 19.05.2016.
 */
public class RxBus {

    private static final RxBus RX_BUS = new RxBus();

    public static RxBus bus() {
        return RX_BUS;
    }

    private RxBus() {
    }

    private final Subject<Object, Object> BUS = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        BUS.onNext(o);
    }

    public Observable<Object> toObservable() {
        return BUS;
    }
}
