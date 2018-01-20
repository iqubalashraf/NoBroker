package app.nobroker.dataModels;

/**
 * Created by ashrafiqubal on 20/01/18.
 */

public class Photos {
    ImagesMap imagesMap = new ImagesMap();
    boolean displayPic = false;
    String name = "";
    String title = "";

    public Photos() {
    }

    public ImagesMap getImagesMap() {

        return imagesMap;
    }

    public void setImagesMap(ImagesMap imagesMap) {
        this.imagesMap = imagesMap;
    }

    public boolean isDisplayPic() {
        return displayPic;
    }

    public void setDisplayPic(boolean displayPic) {
        this.displayPic = displayPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
