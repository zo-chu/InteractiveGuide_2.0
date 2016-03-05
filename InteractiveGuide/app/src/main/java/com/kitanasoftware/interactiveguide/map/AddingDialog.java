package com.kitanasoftware.interactiveguide.map;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kitanasoftware.interactiveguide.R;
import com.kitanasoftware.interactiveguide.db.WorkWithDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chudo on 27.01.2016.
 */
public class AddingDialog extends DialogFragment {
    private int pickedColor;
    private double[] coordinates;
    private Map<String, Integer> typesGeoMap;
    private  EditText editgeoName;
    private  EditText editgeoType;
    private WorkWithDb workWithDb;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        coordinates = getArguments().getDoubleArray("coordinates");
        //get instance for work with db
        workWithDb=WorkWithDb.getWorkWithDb();
        return getAddingDialog();
    }

    private Dialog getAddingDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_addgeo, null);

        editgeoName = (EditText) view.findViewById(R.id.editName);
        editgeoType = (AutoCompleteTextView) view.findViewById(R.id.editType);

        //Color list
        Gallery listOfColors = (Gallery) view.findViewById(R.id.listOfColors);
        final ArrayList<Integer> colors = GeopointsData.getInstance().getCOLORS();
        ColorAdapter colorsAdapter = new ColorAdapter(colors, getActivity().getApplicationContext());
        listOfColors.setAdapter(colorsAdapter);

        final ImageView pickedColorImageview = (ImageView) view.findViewById(R.id.pickedColor);
        listOfColors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pickedColor = colors.get(position);
                pickedColorImageview.setImageResource(pickedColor);

            }
        });

        final LinearLayout colorPickerLayout = (LinearLayout) view.findViewById(R.id.colorPickerLayout);

        final Button pickColor = (Button) view.findViewById(R.id.pickColor);
        pickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerLayout.setVisibility(View.VISIBLE);
            }
        });
        // type of geopoint
        typesGeoMap = GeopointsData.getInstance().getTypesOfGeopoints();
        List<String> typesGeoList = new ArrayList<>();
        typesGeoList.addAll(typesGeoMap.keySet());

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.one_type_geo, typesGeoList);
        final AutoCompleteTextView typeGeoView = (AutoCompleteTextView)
                view.findViewById(R.id.editType);
        typeGeoView.setAdapter(typeAdapter);

        typeGeoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeGeoView.showDropDown();
            }
        });
        typeGeoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pickedType = typeGeoView.getText().toString().toLowerCase();
                if (typesGeoMap.containsKey(pickedType)) {
                    System.out.println(pickedType);
                    pickedColor = typesGeoMap.get(pickedType);
                    pickedColorImageview.setImageResource(pickedColor);
                }
            }
        });


        //if editing mode
        if (MapScreen_5.editingMode) {
            editgeoName.setText(getArguments().getString("name"));
            typeGeoView.setText(getArguments().getString("type"));
            pickedColor = getArguments().getInt("color");
            pickedColorImageview.setImageResource(pickedColor);
        }
        //buttons
        Button okButton = (Button) view.findViewById(R.id.addedOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button addingModeOk = (Button) getActivity().findViewById(R.id.creationGeoOk);
                Button addingModeCancel = (Button) getActivity().findViewById(R.id.creationGeoCancel);
                Button addGeo = (Button) getActivity().findViewById(R.id.addGeopoint);
                Button editGeo = (Button) getActivity().findViewById(R.id.editGeopoints);
                addingModeOk.setVisibility(View.GONE);
                addingModeCancel.setVisibility(View.GONE);
                addGeo.setVisibility(View.VISIBLE);
                editGeo.setVisibility(View.VISIBLE);


                String geoName = editgeoName.getText().toString();
                String geoType = editgeoType.getText().toString().toLowerCase();

                if (geoName.equals("") || geoType.equals("") || pickedColor == 0) {
                    Toast.makeText(getActivity(), "Pick a name, type and color for your geopoint!", Toast.LENGTH_SHORT).show();
                } else {
                    Geopoint newGeopoint = new Geopoint(geoName,
                            geoType,
                            pickedColor,
                            new double[]{coordinates[0], coordinates[1]}
                    );
                    System.out.println( " coor" +coordinates[0]+ " coor " +coordinates[1] );
                    //ArrayList<Geopoint> arrGeo = GeopointsData.getInstance().getGeopoints();

                    //get All geopoints from db
                    ArrayList<Geopoint> arrGeo = workWithDb.getGeopointList();

                    if (MapScreen_5.editingMode) {
                        //arrGeo.set(getArguments().getInt("position"), newGeopoint);
                        //arrGeo.set(getArguments().getInt("position"),newGeopoint);

                        //update in db and in singlton- Work with db
                        WorkWithDb.getWorkWithDb().updateGeopointByIndex(getArguments().getInt("position"),geoName,
                                geoType, pickedColor, new double[]{coordinates[0], coordinates[1]});

                    } else {
                        //arrGeo.add(newGeopoint);
                        WorkWithDb.getWorkWithDb().addGeopiont(geoName,
                                geoType, pickedColor, new double[]{coordinates[0], coordinates[1]});

                    }

                    //GeopointsData.getInstance().setGeopoints(arrGeo);

                    typesGeoMap = GeopointsData.getInstance().getTypesOfGeopoints();
                    typesGeoMap.put(geoType, pickedColor);
                    GeopointsData.getInstance().setTypesOfGeopoints(typesGeoMap);

                    ((MapScreen_5) getActivity()).createOverlay();
                    MapScreen_5.editingMode = false;
                    MapScreen_5.addingMode = false;

                    dismiss();
                    Toast toast = Toast.makeText(getActivity(), "Geopoint added", Toast.LENGTH_SHORT);
                    System.out.println(workWithDb.getGeopointList().get(0).getName() + " coord " + workWithDb.getGeopointList().get(0).getCoordinates()[0]+ "  p "+ workWithDb.getGeopointList().get(0).getCoordinates()[1]);
                    toast.show();

                }
            }}

        );

        Button cancButton = (Button) view.findViewById(R.id.addedCancel);
        cancButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              dismiss();
                                          }
                                      }
        );

        builder.setView(view);
        return builder.create();
    }
}
