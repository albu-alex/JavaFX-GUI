package model.ADT;

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
