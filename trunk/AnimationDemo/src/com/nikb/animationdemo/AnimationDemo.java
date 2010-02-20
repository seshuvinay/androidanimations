package com.nikb.animationdemo;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AnimationDemo extends Activity
{
	private static final String TAG = AnimationDemo.class.getSimpleName();
	private Interpolator selectedInterpolator;
	private Interpolator[] availableInterpolators;
	private Animation[] animations;
	private Random random = new Random();
	private EditText durationText;
	private int selectedAnimationDuration;
	private Animation selectedAnimation;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		availableInterpolators = new Interpolator[] { new AccelerateDecelerateInterpolator(), new AccelerateInterpolator(), new AnticipateInterpolator(), new AnticipateOvershootInterpolator(),
				new BounceInterpolator(), new CycleInterpolator(3), new DecelerateInterpolator(), new LinearInterpolator(), new OvershootInterpolator() };

		animations = new Animation[] { AnimationUtils.loadAnimation(this, R.anim.translateanimation), AnimationUtils.loadAnimation(this, R.anim.alphaanimation),
				AnimationUtils.loadAnimation(this, R.anim.rotateanimation), AnimationUtils.loadAnimation(this, R.anim.scaleanimation)

		};
		//hide the title
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		final View image = findViewById(R.id.image);

		// setup the animation type spinner
		final Spinner animationSpinner = (Spinner) findViewById(R.id.animationType);

		ArrayAdapter animationAdapter = ArrayAdapter.createFromResource(this, R.array.animations, R.layout.choice);
		animationAdapter.setDropDownViewResource(R.layout.dropdownchoices);
		animationSpinner.setAdapter(animationAdapter);

		// setup the interpolator type spinner
		final Spinner spinner = (Spinner) findViewById(R.id.spinner);
		durationText = (EditText) findViewById(R.id.duration);

		final ArrayList<InterpolatorHolder> arrayList = new ArrayList<InterpolatorHolder>();
		for (int i = 0; i < availableInterpolators.length; i++)
		{
			arrayList.add(new InterpolatorHolder(availableInterpolators[i]));
		}
		ArrayAdapter<InterpolatorHolder> arrayAdapter = new ArrayAdapter<InterpolatorHolder>(this, R.layout.choice, arrayList);
		arrayAdapter.setDropDownViewResource(R.layout.dropdownchoices);
		spinner.setAdapter(arrayAdapter);

		// add listener to the edit text
		durationText.setOnTouchListener(new View.OnTouchListener()
		{

			public boolean onTouch(View v, MotionEvent event)
			{
				durationText.selectAll();
				return false;
			}
		});

		// hook up the Go button
		final Button button = (Button) findViewById(R.id.play);
		button.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				// hide the keyboard if visible
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(button.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

				// setup the animation and interpolator and start the animation
				selectedInterpolator = availableInterpolators[spinner.getSelectedItemPosition()];
				;
				selectedAnimation = animations[animationSpinner.getSelectedItemPosition()];
				selectedAnimation.setInterpolator(selectedInterpolator);
				setDuration();
				selectedAnimation.setDuration(selectedAnimationDuration);
				image.startAnimation(selectedAnimation);
			}
		});
	}

	private void setDuration()
	{
		// if the user has not entered a proper number, correct it
		try
		{
			selectedAnimationDuration = Integer.parseInt(durationText.getText().toString());
		} catch (Exception e)
		{
			selectedAnimationDuration = 700;
			durationText.setText("" + selectedAnimationDuration);
		}

	}

	static class InterpolatorHolder
	{
		Interpolator interpolator;

		InterpolatorHolder(Interpolator interpolator)
		{
			this.interpolator = interpolator;
		}

		@Override
		public String toString()
		{
			return interpolator.getClass().getSimpleName();
		}
	}

}