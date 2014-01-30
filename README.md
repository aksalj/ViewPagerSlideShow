#ViewPager SlideShow
===================

ViewPageSlideShow is a simple transaformation of Android's `ViewPager` into a slide show widget that displays image from URLs as well as app resources.


##Features

- Play, stop, next & previous
- Slide timer & transitions
- Asynchronous loading of images, loading happens outside the UI thread
- Images are cached to memory and to disk for super fast loading


##Usage

*For a working implementation of this project see the `exmaple/` folder.*

  1. Include the widget in your view.

        <com.aksalj.viewpagerslideshow.ViewPagerSlideshow
            android:id="@+id/slideshow"
            android:layout_height="wrap_content"
            android:layout_width="match_parent" />

  2. In your `onCreate` method (or `onCreateView` for a fragment), bind the
     indicator to the `ViewPager`.

		 //Have your image resources
		 //int[] IMAGES = {R.drawable.img_1, R.drawable.img_2};
		 // or urls
		 String[] IMAGES = {"http://somesite.xx/img.png", "http://someothersite.xc/img.jpg"};
		 

         //Set the pager with an adapter
         ViewPager slideshow = (pagerSlideshow)findViewById(R.id.slideshow);
         SlideShowAdapter adapter = new SlideShowAdapter(getSupportFragmentManager(), IMAGES);
         slideshow.setAdapter(adapter);
         
  3. Now you can play/stop, the slide show
  			
  		 //continued from above
  		 slideshow.play();
  		 slideshow.next();
  		 slideshow.prev();
  		 slideshow.stop();
  		 

  4. *(Optional)* If you what to listen to slide change, use an `OnPageChangeListener` and set it as follows:

         //continued from above
         slideshow.setOnSlideChangeListener(mPageChangeListener);
         //or
         slideshow.setOnPageChangeListener(mPageChangeListener);
 
 This project depends on the `ViewPager` class which is available in the [Android Support Library]() or [ActionBarSherlock](). 

##Customization

##Credit