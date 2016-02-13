package com.kitanasoftware.interactiveguide.map;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import com.kitanasoftware.interactiveguide.R;

import java.util.ArrayList;

/**
 * Created by Chudo on 26.01.2016.
 */
public class EditingDialog extends DialogFragment {
    private ArrayList<Geopoint> geopoints;
    private GeopointsAdapter adapter;
    private HybridMap mapView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        geopoints = GeopointsData.getInstance().getGeopoints();
        mapView = (HybridMap) getActivity().findViewById(R.id.hybridMap);

        adapter = new GeopointsAdapter(getActivity(), geopoints);

        if (getArguments().getString("clicked") == "edit") {
            return getEditingDialog();
        } else {
            return getAllPointsList();
        }
    }

    private Dialog getAllPointsList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(" ")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double[] thisCoords = geopoints.get(which).getCoordinates();
                        mapView.center(thisCoords[0], thisCoords[1]);
                        dismiss();
                    }
                });

        return builder.create();
    }

    private Dialog getEditingDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.editGeopointMess)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double[] thisCoords = geopoints.get(which).getCoordinates();
                        mapView = (HybridMap) getActivity().findViewById(R.id.hybridMap);
                        mapView.center(thisCoords[0], thisCoords[1]);
                        ((EditGeo) getActivity()).setEditableGeo(geopoints.get(which));
                        ((EditGeo) getActivity()).setEditedPositionInList(which);
                        editingModeOn();
                        dismiss();
                    }
                });

        return builder.create();
    }

    private void editingModeOn() {
        MapScreen_5.editingMode = true;
        Button addingModeOk = (Button) getActivity().findViewById(R.id.creationGeoOk);
        Button addingModeCancel = (Button) getActivity().findViewById(R.id.creationGeoCancel);
        Button addGeo = (Button) getActivity().findViewById(R.id.addGeopoint);
        Button editGeo = (Button) getActivity().findViewById(R.id.editGeopoints);
        Button delGeo = (Button) getActivity().findViewById(R.id.delGeopoints);

        delGeo.setVisibility(View.VISIBLE);
        addingModeOk.setVisibility(View.VISIBLE);
        addingModeCancel.setVisibility(View.VISIBLE);
        addGeo.setVisibility(View.GONE);
        editGeo.setVisibility(View.GONE);
    }
}

