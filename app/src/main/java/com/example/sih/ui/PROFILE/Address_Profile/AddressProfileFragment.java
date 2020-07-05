package com.example.sih.ui.PROFILE.Address_Profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.sih.R;
import com.example.sih.ui.PROFILE.Agri_Profile.AgriProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.HashMap;

import static com.example.sih.MainActivity_registered.navigationView;

public class AddressProfileFragment extends Fragment {

    private AddressProfileViewModel addressProfileViewModel;
    Button submit;
    EditText al1, al2, pin, cityet;
    Spinner statespin, districtspin;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser firebaseUser;
    DatabaseReference myRef;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addressProfileViewModel = ViewModelProviders.of(this).get(AddressProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile_address, container, false);

        submit = root.findViewById(R.id.submit);
        al1 = root.findViewById(R.id.al1);
        al2 = root.findViewById(R.id.al2);
        pin = root.findViewById(R.id.pin);
        cityet = root.findViewById(R.id.city);
        statespin = root.findViewById(R.id.state);
        districtspin = root.findViewById(R.id.district);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (al1.getText().toString().trim().equals("") || al2.getText().toString().trim().equals("")
                        || pin.getText().toString().trim().equals("") || cityet.getText().toString().trim().equals("")
                        || statespin.getSelectedItem() == null || districtspin.getSelectedItem() == null ||
                        statespin.getSelectedItem().toString().equals("STATE") || districtspin.getSelectedItem().toString().equals("DISTRICT")) {
                    AlertDialog.Builder bd = new AlertDialog.Builder(getContext());
                    bd.setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = bd.create();
                    alertDialog.setTitle("Please enter all details properly !!!");
                    alertDialog.show();

                } else {
                    String userid = firebaseUser.getUid();

                    myRef = database.getReference("Addresses").child(userid);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("Address", al1.getText().toString() + " , " + al2.getText().toString() + " , " + cityet.getText().toString()
                            + " , " + districtspin.getSelectedItem().toString() + " , " + statespin.getSelectedItem().toString()
                            + " , " + pin.getText().toString());
                    myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                AlertDialog.Builder bd = new AlertDialog.Builder(getContext());
                                bd.setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Fragment fragment = new AgriProfileFragment();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.main_content, fragment)
                                                .commit();
                                        navigationView.setCheckedItem(R.id.nav_pro_3);
                                    }
                                });
                                AlertDialog alertDialog = bd.create();
                                alertDialog.setTitle("Thank you for the details");
                                alertDialog.show();
                            }
                        }
                    });
                }
            }
        });
        return root;
    }
}