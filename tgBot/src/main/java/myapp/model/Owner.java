package myapp.model;

public class Owner {
    private long id = 588484700L;
    private static Owner instance;

    public static Owner getInstance() {
        Owner localInstance = instance;
        if (localInstance == null) {
            synchronized (Owner.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Owner();
                }
            }
        }
        return localInstance;
    }

    private Owner() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
