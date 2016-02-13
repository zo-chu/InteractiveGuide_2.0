package com.kitanasoftware.interactiveguide.map;

/**
 * Created by Chudo on 31.01.2016.
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;

import com.kitanasoftware.interactiveguide.R;

public class MyItemizedOverlay extends ItemizedIconOverlay<OverlayItem> {

    protected Context mContext;
    private ArrayList<OverlayItem> items;
    private OverlayItem item;

    @Override
    public void addItem(int location, OverlayItem item) {
        super.addItem(location, item);
    }

    public MyItemizedOverlay(final Context context, final ArrayList<OverlayItem> aList) {
        super(context, aList,
                new OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index,
                                                     final OverlayItem item) {
                        return false;
                    }

                    @Override
                    public boolean onItemLongPress(final int index,
                                                   final OverlayItem item) {
                        return false;
                    }
                });
        // TODO Auto-generated constructor stub
        mContext = context;
        items = aList;
    }

    @Override
    public boolean addItem(OverlayItem item) {
        // TODO Auto-generated method stub
        return super.addItem(item);
    }

    @Override
    protected boolean onSingleTapUpHelper(final int index,
                                          final OverlayItem item, final MapView mapView) {
        if(!item.getTitle().equals("NewPoint")) {
            Toast.makeText(mContext, "" + item.getTitle() + " " + item.getSnippet(),
                    Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e, MapView mapView) {
        float x = e.getX();
        float y = e.getY();

        if (MapScreen_5.addingMode) {

            createGeo(mapView, (int) x, (int) y, R.drawable.test_icon);
        }
        if (MapScreen_5.editingMode) {

            Geopoint editedGeo = ((EditGeo) mContext).getEditableGeo();
            int editedPosition = ((EditGeo) mContext).getEditedPositionInList();

            // if this geopoint exist in the list - remove it
            double inListLongit = new BigDecimal(items.get(editedPosition).
                    getPoint().getLongitude()).setScale(6, RoundingMode.HALF_UP).doubleValue();
            double inListLаtit = new BigDecimal(items.get(editedPosition).
                    getPoint().getLatitude()).setScale(6, RoundingMode.HALF_UP).doubleValue();

            System.out.println(inListLongit +" "+ editedGeo.getCoordinates()[1]);
            System.out.println(inListLаtit+ " "+  editedGeo.getCoordinates()[0]);
            if (inListLongit == editedGeo.getCoordinates()[1] &&
                    inListLаtit == editedGeo.getCoordinates()[0]) {
                items.remove(editedPosition);
            }
            //create new one instead
            createGeo(mapView, (int) x, (int) y, editedGeo.getColor());
        }
        return super.onSingleTapUp(e, mapView);
    }

    private void createGeo(MapView mapView, int x, int y, int markerRes) {
        GeoPoint thisGeoPoint = (GeoPoint) mapView.getProjection().fromPixels(x, y);
        item = new OverlayItem("NewPoint", "NewType", thisGeoPoint);
        Drawable marker = mContext.getResources().getDrawable(
                markerRes);
        item.setMarker(marker);
        if (items.get(items.size() - 1).getTitle().equals("NewPoint")) {
            items.remove(items.size() - 1);
        }
        addItem(item);
        mapView.invalidate();
    }
}