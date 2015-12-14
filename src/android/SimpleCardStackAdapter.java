package com.andtinder.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.andtinder.model.CardModel;
import com.digitalvibes.swipeCards.MainActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class SimpleCardStackAdapter extends CardStackAdapter {

	String package_name = MainActivity.PACKAGE_NAME;
	Resources resources = MainActivity.RESOURCES;
	public SimpleCardStackAdapter(Context mContext) {
		super(mContext);
	}

	@Override
	public View getCardView(final int position, CardModel model, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(resources.getIdentifier("std_card_inner","layout",package_name), parent, false);
			assert convertView != null;
		}

		ImageView placeHolderImage = (ImageView) convertView.findViewById(resources.getIdentifier("imageView", "id", package_name));
		new ImageLoadTask(model.getDescription(),
				placeHolderImage).execute();
		//((ImageView) convertView.findViewById(resources.getIdentifier("imageView", "id", package_name))).setImageDrawable(model.getCardImageDrawable());
		((TextView) convertView.findViewById(resources.getIdentifier("textPlaceHolder","id",package_name))).setText(model.getTitle());

		Button right = (Button) convertView.findViewById(resources.getIdentifier("likeButton","id",package_name));
		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getCardModel(position).getParentCardContainer().flingRight();
				getCardModel(position).getOnCardDismissedListener().onLike();
			}
		});

		Button left = (Button) convertView.findViewById(resources.getIdentifier("dislikeButton","id",package_name));
		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getCardModel(position).getParentCardContainer().flingLeft();
				getCardModel(position).getOnCardDismissedListener().onDislike();
			}
		});

		return convertView;
	}

	public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

		private String url;
		private ImageView imageView;

		public ImageLoadTask(String url, ImageView imageView) {
			this.url = url;
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			try {
				URL urlConnection = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) urlConnection
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			imageView.setImageBitmap(result);
		}

	}
}
