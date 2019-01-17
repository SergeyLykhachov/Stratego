package com.yahoo.slykhachov.strategone.view;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class PieceView {
	private BufferedImage image;
	private BufferedImage reverseImage;
	private Point2D.Double point;
	public PieceView(Point2D.Double point, BufferedImage image,
			BufferedImage reverseImage) {
		this.point = point;
		this.image = image;
		this.reverseImage = reverseImage;
	}
	public BufferedImage getPieceImage() {
		return this.image;
	}
	public BufferedImage getReverseImge() {
		return this.reverseImage;
	}
	public Point2D.Double getPoint() {
		return this.point;
	}
	public void setPoint(Point2D.Double point) {
		this.point = point;
	}
}
