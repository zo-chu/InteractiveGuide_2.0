package com.kitanasoftware.interactiveguide.map;

/**
 * Created by Chudo on 09.02.2016.
 */
public interface EditGeo {

    void setEditableGeo(Geopoint geopoint);

    Geopoint getEditableGeo();
    int getEditedPositionInList();
    void  setEditedPositionInList(int editedPositionInList);

}
