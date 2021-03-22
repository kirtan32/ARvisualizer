package com.example.arvisualizer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class ObjplacerFragment extends Fragment
{

    private static final String TAG = "Obj.Placer.Activity";

    private ArFragment arFragment;
    private AnchorNode anchorNode;

    private ArrayList<Integer> mImageUrls = new ArrayList<>();
    private ArrayList<Integer> sfbfiles = new ArrayList<>();

    private RecyclerView recyclerView;

    private Context context;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        context = getContext();
        View currentview = inflater.inflate(R.layout.objplacer_fragment,container,false);

        recyclerView = (RecyclerView)currentview.findViewById(R.id.recyclerView);

        // get images into Arraylist
        getImages();
        // get .sfb files into Arraylist
        getSFBs();

        arFragment = (ArFragment) getFragmentManager().findFragmentById(R.id.arfragmentobjectplacer);

        /*

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            placeModel(hitResult.createAnchor());

        });

         */

        Toast.makeText(getContext(),"Object placer Fragment Created",Toast.LENGTH_SHORT).show();

        return currentview;

    }

    private void getImages()
    {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        Field[] fields=R.drawable.class.getFields();
        for(int count=0; count < fields.length; count++){
            try{
                if(fields[count].getName().length()>9)
                {
                    if(fields[count].getName().substring(0,9).equals("objplacer")) {
                        // geting IDs of images
                        mImageUrls.add(fields[count].getInt(fields[count]));
                    }
                }

            }
            catch(Exception e)
            {
            }

        }

        initRecyclerView();
    }

    private void getSFBs(){
        Log.d(TAG, "preparing sfb files");

        Field[] fields=R.raw.class.getFields();
        for(int count=0; count < fields.length; count++){
            try{
                sfbfiles.add(fields[count].getInt(fields[count]));
            }
            catch(Exception e)
            {
            }

        }

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        //Toast.makeText(this,"initRecyclerview",Toast.LENGTH_SHORT).show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // creating class object
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(context, mImageUrls);

        recyclerView.setAdapter(adapter);
    }

    private void placeModel(Anchor anchor)
    {
        ModelRenderable.builder()
                .setSource(context, sfbfiles.get(RecyclerViewAdapter.counter))
                .build()
                .thenAccept(modelRenderable -> addModelToScene(anchor,modelRenderable))
                .exceptionally(throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(throwable.getMessage())
                            .show();
                    return null;
                });

    }

    private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable)
    {
        // we cant increse or decrese size of anchornode
        anchorNode = new AnchorNode(anchor);
        // for zoom in zoom out
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        // tranformableNode is child of anchorNode
        transformableNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode); // here place parent node not child(transformableNode)
        transformableNode.select();  // we select it
    }


}
