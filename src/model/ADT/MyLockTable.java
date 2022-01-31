package model.ADT;

//I created a new MyLockTable ADT that extends the MyDictionary ADT because the MyDictionary
//implementation does not take into account synchronization between threads
public class MyLockTable<TKey, TValue> extends MyDictionary<TKey, TValue> {
    int firstAvailablePosition = 1;

    public MyLockTable(){
        super();
    }

    private synchronized void setNextAvailablePosition(){
        firstAvailablePosition++;
    }

    public synchronized int getFirstAvailablePosition(){
        setNextAvailablePosition();
        return firstAvailablePosition;
    }

    @Override
    public void clear() {
        dictionary.clear();
        firstAvailablePosition = 1;
    }
}
