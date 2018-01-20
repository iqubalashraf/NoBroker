package app.nobroker.dataModels;

/**
 * Created by ashrafiqubal on 20/01/18.
 */

public class Score {
    long lastUpdatedDate = 0;
    float transit = 0.0f;
    float lifestyle = 0;

    public Score() {

    }

    public long getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(long lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public float getTransit() {
        return transit;
    }

    public void setTransit(float transit) {
        this.transit = transit;
    }

    public float getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(float lifestyle) {
        this.lifestyle = lifestyle;
    }
}
