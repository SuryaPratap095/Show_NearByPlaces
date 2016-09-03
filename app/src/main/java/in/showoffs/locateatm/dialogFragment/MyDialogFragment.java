package in.showoffs.locateatm.dialogFragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.showoffs.locateatm.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDialogFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<String> mList;
    private String mParam2;
    RelativeLayout Rl_Title,Rl_Search;
    ImageButton btn_search,btn_bk_tittle,btn_erase_srTXT;
    ListView listView;
    TextView title;
   EditText searchView;
    Button ok;
    InputMethodManager imm;
    ArrayList<String> list;
    private OnDialogFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyDialogFragment newInstance(ArrayList<String> param1, String param2)
    {
        MyDialogFragment fragment = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyDialogFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mList = getArguments().getStringArrayList(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_my_dialog, container, false);

        Rl_Title= (RelativeLayout) rootView.findViewById(R.id.RL_Header_Title);
        Rl_Search= (RelativeLayout) rootView.findViewById(R.id.RL_Header_Search);

        listView= (ListView) rootView.findViewById(R.id.listView);

        searchView= (EditText) rootView.findViewById(R.id.ed_searchView);

        btn_search= (ImageButton) rootView.findViewById(R.id.searchButton);
        btn_bk_tittle= (ImageButton) rootView.findViewById(R.id.btn_backToHeaderTitle);
        btn_erase_srTXT= (ImageButton) rootView.findViewById(R.id.btn_eraseTxt_searchView);
        title=(TextView)rootView.findViewById(R.id.tv_title);
        ok=(Button)rootView.findViewById(R.id.btn_ok);

        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        title.setText(mParam2);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Rl_Search.setVisibility(View.VISIBLE);
                Rl_Title.setVisibility(View.GONE);
                searchView.requestFocus();

                imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        btn_bk_tittle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Rl_Title.setVisibility(View.VISIBLE);
                Rl_Search.setVisibility(View.GONE);
                searchView.setText("");
          //      imm.hideSoftInputFromInputMethod( btn_bk_tittle.getWindowToken(),0);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        btn_erase_srTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchView.setText("");

               /* String textString = searchView.getText().toString();
                if( textString.length() > 0 ) {
                    searchView.setText(textString.substring(0, textString.length() - 1 ));
                    searchView.setSelection(searchView.getText().length());//position cursor at the end of the line
                }*/
            }
        });





        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_activated_1,mList);
        listView.setAdapter(adapter);
        ColorDrawable sage= new ColorDrawable(Color.GRAY);
        listView.setDivider(sage);
        listView.setDividerHeight(1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mListener.onDialogFragmentInteraction(adapter.getItem(position), position);
                searchView.setText("");
                dismiss();
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String value, int position)
    {
        if (mListener != null)
        {
            mListener.onDialogFragmentInteraction( value, position);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnDialogFragmentInteractionListener) activity;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDialogFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onDialogFragmentInteraction(String value, int position);
    }

}
