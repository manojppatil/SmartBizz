package com.eduvanzapplication.newUI.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eduvanzapplication.R;

import moe.feng.common.stepperview.VerticalStepperItemView;

public class KycDetailFragment extends Fragment {

	private VerticalStepperItemView mSteppers[] = new VerticalStepperItemView[3];
	private Button mNextBtn0, mNextBtn1, mPrevBtn1, mNextBtn2, mPrevBtn2;

    private VerticalStepperItemView mSteppersCoBr[] = new VerticalStepperItemView[2];
    private Button mNextBtn0CoBr, mNextBtn1CoBr, mPrevBtn1CoBr;

    private int mActivatedColorRes = R.color.material_blue_500;
	private int mDoneIconRes = R.drawable.ic_done_white_16dp;

    static View view;

	public static Context context;
	public static Fragment mFragment;
    public static RelativeLayout relborrower, relCoborrower;
    public static LinearLayout linBorrowerForm, linCoCorrowerForm;
	public static TextView txtBorrowerArrowKey, txtCoBorrowerArrowKey;
	public static int borrowerVisiblity = 0, coborrowerVisiblity = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_kycdetail_stepper, parent, false);

        linBorrowerForm = (LinearLayout) view.findViewById(R.id.linBorrowerForm);
        linCoCorrowerForm = (LinearLayout) view.findViewById(R.id.linCoCorrowerForm);

        relborrower = (RelativeLayout) view.findViewById(R.id.relborrower);
        relCoborrower = (RelativeLayout) view.findViewById(R.id.relCoborrower);

        txtBorrowerArrowKey = (TextView) view.findViewById(R.id.txtBorrowerArrowKey);
        txtCoBorrowerArrowKey = (TextView) view.findViewById(R.id.txtCoBorrowerArrowKey);

		context = getContext();
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		if (borrowerVisiblity == 0) {
			linBorrowerForm.setVisibility(View.VISIBLE);
			borrowerVisiblity = 1;
			txtBorrowerArrowKey.setText(getResources().getString(R.string.up));
		} else if (borrowerVisiblity == 1) {
			linBorrowerForm.setVisibility(View.GONE);
			borrowerVisiblity = 0;
			txtBorrowerArrowKey.setText(getResources().getString(R.string.down));
		}
		if (coborrowerVisiblity == 0) {
			linCoCorrowerForm.setVisibility(View.VISIBLE);
			coborrowerVisiblity = 1;
			txtCoBorrowerArrowKey.setText(getResources().getString(R.string.up));
		} else if (coborrowerVisiblity == 1) {
			linCoCorrowerForm.setVisibility(View.GONE);
			coborrowerVisiblity = 0;
			txtCoBorrowerArrowKey.setText(getResources().getString(R.string.down));
		}

		relborrower.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (borrowerVisiblity == 0) {
					linBorrowerForm.setVisibility(View.VISIBLE);
					borrowerVisiblity = 1;
					txtBorrowerArrowKey.setText(getResources().getString(R.string.up));
				} else if (borrowerVisiblity == 1) {
					linBorrowerForm.setVisibility(View.GONE);
					borrowerVisiblity = 0;
					txtBorrowerArrowKey.setText(getResources().getString(R.string.down));
				}

			}
		});

		relCoborrower.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (coborrowerVisiblity == 0) {
					linCoCorrowerForm.setVisibility(View.VISIBLE);
					coborrowerVisiblity = 1;
					txtCoBorrowerArrowKey.setText(getResources().getString(R.string.up));
//					setCoborrower();

				} else if (coborrowerVisiblity == 1) {
					linCoCorrowerForm.setVisibility(View.GONE);
					coborrowerVisiblity = 0;
					txtCoBorrowerArrowKey.setText(getResources().getString(R.string.down));
				}

			}
		});
        return view;
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

	    mSteppers[0] = view.findViewById(R.id.stepper_0);
		mSteppers[1] = view.findViewById(R.id.stepper_1);
		mSteppers[2] = view.findViewById(R.id.stepper_2);

		VerticalStepperItemView.bindSteppers(mSteppers);

		mNextBtn0 = view.findViewById(R.id.button_next_0);
		mNextBtn0.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSteppers[0].nextStep();
			}
		});

		mSteppers[0].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSteppers[0].nextStep();
			}
		});

		mSteppers[1].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSteppers[0].nextStep();
			}
		});

		mSteppers[2].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSteppers[1].nextStep();
			}
		});

		view.findViewById(R.id.button_test_error).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mSteppers[0].getErrorText() != null) {
					mSteppers[0].setErrorText(null);
				} else {
					mSteppers[0].setErrorText("Test error!");
				}
			}
		});

		mPrevBtn1 = view.findViewById(R.id.button_prev_1);
		mPrevBtn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSteppers[1].prevStep();
			}
		});

		mNextBtn1 = view.findViewById(R.id.button_next_1);
		mNextBtn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSteppers[1].nextStep();
			}
		});

		mPrevBtn2 = view.findViewById(R.id.button_prev_2);
		mPrevBtn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSteppers[2].prevStep();
			}
		});

		mNextBtn2 = view.findViewById(R.id.button_next_2);
		mNextBtn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Finish!", Snackbar.LENGTH_LONG).show();
			}
		});

        mSteppersCoBr[0] = view.findViewById(R.id.stepper_0CoBr);
        mSteppersCoBr[1] = view.findViewById(R.id.stepper_1CoBr);

        VerticalStepperItemView.bindSteppers(mSteppers);

        mNextBtn0CoBr = view.findViewById(R.id.button_next_0CoBr);
        mNextBtn0CoBr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSteppersCoBr[0].nextStep();
            }
        });

        mSteppersCoBr[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSteppersCoBr[0].nextStep();
            }
        });

        mSteppersCoBr[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSteppersCoBr[0].nextStep();
            }
        });

        view.findViewById(R.id.button_test_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSteppersCoBr[0].getErrorText() != null) {
                    mSteppersCoBr[0].setErrorText(null);
                } else {
                    mSteppersCoBr[0].setErrorText("Test error!");
                }
            }
        });

        mPrevBtn1CoBr = view.findViewById(R.id.button_prev_1CoBr);
        mPrevBtn1CoBr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSteppersCoBr[1].prevStep();
            }
        });

        mNextBtn1CoBr = view.findViewById(R.id.button_next_1CoBr);
        mNextBtn1CoBr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSteppersCoBr[1].nextStep();
            }
        });

		view.findViewById(R.id.btn_change_point_color).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mActivatedColorRes == R.color.material_blue_500) {
					mActivatedColorRes = R.color.material_deep_purple_500;
				} else {
					mActivatedColorRes = R.color.material_blue_500;
				}
				for (VerticalStepperItemView stepper : mSteppers) {
					stepper.setActivatedColorResource(mActivatedColorRes);
				}
			}
		});
		view.findViewById(R.id.btn_change_done_icon).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mDoneIconRes == R.drawable.ic_done_white_16dp) {
					mDoneIconRes = R.drawable.ic_save_white_16dp;
				} else {
					mDoneIconRes = R.drawable.ic_done_white_16dp;
				}
				for (VerticalStepperItemView stepper : mSteppers) {
					stepper.setDoneIconResource(mDoneIconRes);
				}
			}
		});
	}




}
