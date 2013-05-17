package hype.drawable;

import hype.util.H;
import processing.core.PGraphics;
import processing.core.PImage;

public class HImage extends HDrawable {
	protected PImage _image;

	public HImage() {
		this(null);
	}
	
	public HImage(Object imgArg) {
		image(imgArg);
	}
	
	@Override
	public HImage createCopy() {
		HImage copy = new HImage(_image);
		copy.copyPropertiesFrom(this);
		return copy;
	}
	
	public HImage resetSize() {
		if(_image == null) size(0f,0f);
		else size(_image.width, _image.height);
		return this;
	}
	
	public HImage image(Object imgArg) {
		if(imgArg instanceof PImage) {
			_image = (PImage) imgArg;
		} else if(imgArg instanceof String) {
			_image = H.app().loadImage((String) imgArg);
		} else if(imgArg instanceof HImage) {
			_image = ((HImage) imgArg)._image;
		} else if(imgArg == null) {
			_image = null;
		}
		return resetSize();
	}
	
	public PImage image() {
		return _image;
	}
	
	public HImage tint(int clr) {
		fill(clr);
		return this;
	}
	
	public HImage tint(int clr, int alpha) {
		fill(clr, alpha);
		return this;
	}
	
	public HImage tint(int r, int g, int b) {
		fill(r,g,b);
		return this;
	}
	
	public HImage tint(int r, int g, int b, int a) {
		fill(r,g,b,a);
		return this;
		
	}
	
	public int tint() {
		return fill();
	}
	
	@Override
	public boolean containsRel(float relX, float relY) {
		if(_image == null ||
				_image.width <= 0 || _image.height <= 0 ||
				_width <= 0 || _height <= 0)
			return false;
		int ix = Math.round(relX * _image.width/_width);
		int iy = Math.round(relY * _image.height/_height);
		return (0 < _image.get(ix,iy)>>>24);
	}
	
	@Override
	public void draw( PGraphics g, boolean usesZ,
		float drawX, float drawY, float currAlphaPerc
	) {
		if(_image==null) return;
		
		// This awkward alpha separation is due to pjs compatibility issues
		currAlphaPerc *= (_fill>>>24);
		g.tint( _fill | 0xFF000000, Math.round(currAlphaPerc) );
		
		// Determine if the image will be flipped
		int wscale = 1;
		int hscale = 1;
		float w = _width;
		float h = _height;
		if(_width < 0) {
			w = -_width;
			wscale = -1;
			drawX = -drawX;
		}
		if(_height < 0) {
			h = -_height;
			hscale = -1;
			drawY = -drawY;
		}
		
		// Flip and draw the image
		g.pushMatrix();
			g.scale(wscale, hscale);
			g.image(_image, drawX,drawY, w,h);
		g.popMatrix();
	}
}
