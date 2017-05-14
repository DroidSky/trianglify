package com.sdsmdg.kd.trianglify.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sdsmdg.kd.trianglify.presenters.Presenter;
import com.sdsmdg.kd.trianglify.R;
import com.sdsmdg.kd.trianglify.models.Palette;
import com.sdsmdg.kd.trianglify.models.Triangulation;
import com.sdsmdg.kd.trianglify.utilities.triangulator.Triangle2D;

public class TrianglifyView extends View implements TrianglifyViewInterface{
    int bleedX;
    int bleedY;
    int gridHeight;
    int gridWidth;
    int typeGrid;
    int variance;
    int cellSize;

    /**
     * flag for change in attributes
     * if triangulation is unchanged then value is -1
     * if change in grid width, grid height, variance, bleedX, bleedY, typeGrid or cell size then value is 1
     * if change in palette or random coloring then value is 2
     * if change in fillTriangle or drawStroke then value is 0
     */
    int flagForChangeInRelatedParameters = -1;

    boolean fillTriangle;
    boolean drawStroke;
    boolean randomColoring;

    int paletteNumber;

    Palette palettesArray[] = {Palette.YlGn, Palette.YlGnBu, Palette.GnBu, Palette.BuGn, Palette.PuBuGn,
            Palette.PuBu, Palette.BuPu, Palette.RdPu, Palette.PuRd, Palette.OrRd, Palette.YlOrRd,
            Palette.YlOrBr, Palette.Purples, Palette.Blues, Palette.Greens, Palette.Oranges,
            Palette.Reds, Palette.Greys, Palette.PuOr, Palette.BrBG, Palette.PRGn, Palette.PiYG,
                Palette.RdBu, Palette.RdGy, Palette.RdYlBu, Palette.Spectral, Palette.RdYlGn};
    Palette palette;

    Triangulation triangulation;
    Presenter presenter;

    public TrianglifyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TrianglifyView, 0, 0);
        attributeSetter(a);
        this.presenter = new Presenter(this);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        gridWidth = width;
        gridHeight =height;
    }

    public void attributeSetter(TypedArray typedArray){
        try{
            bleedX = (int) typedArray.getDimension(R.styleable.TrianglifyView_bleedX, 0);
            bleedY = (int) typedArray.getDimension(R.styleable.TrianglifyView_bleedY, 0);
            variance = (int) typedArray.getDimension(R.styleable.TrianglifyView_variance, 10);
            cellSize = (int) typedArray.getDimension(R.styleable.TrianglifyView_cellSize, 40);
            typeGrid = typedArray.getInt(R.styleable.TrianglifyView_gridType, 0);
            fillTriangle = typedArray.getBoolean(R.styleable.TrianglifyView_fillTriangle, true);
            drawStroke = typedArray.getBoolean(R.styleable.TrianglifyView_fillStrokes, false);
            paletteNumber = typedArray.getInt(R.styleable.TrianglifyView_palette, 0);
            palette = palettesArray[paletteNumber];
            typeGrid = GRID_RECTANGLE;
            randomColoring = typedArray.getBoolean(R.styleable.TrianglifyView_randomColoring, false);
        }finally {
            typedArray.recycle();
        }
    }

    @Override
    public Palette getPalette() {
        return palette;
    }

    public TrianglifyView setPalette(Palette palette) {
        this.palette = palette;
        if(this.flagForChangeInRelatedParameters != 1){
            this.flagForChangeInRelatedParameters = 2;
        }
        return this;
    }

    @Override
    public int getCellSize() {
        return cellSize;
    }

    public TrianglifyView setCellSize(int cellSize) {
        this.cellSize = cellSize;
        this.flagForChangeInRelatedParameters = 1;
        return this;
    }

    @Override
    public int getGridHeight() {
        return gridHeight;
    }

    public TrianglifyView setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
        this.flagForChangeInRelatedParameters = 1;
        return this;
    }

    @Override
    public int getGridWidth() {
        return gridWidth;
    }

    public TrianglifyView setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
        this.flagForChangeInRelatedParameters = 1;
        return this;
    }

    @Override
    public int getBleedX() {
        return bleedX;
    }

    public TrianglifyView setBleedX(int bleedX) {
        this.bleedX = bleedX;
        this.flagForChangeInRelatedParameters = 1;
        return this;
    }

    @Override
    public int getBleedY() {
        return bleedY;
    }

    public TrianglifyView setBleedY(int bleedY) {
        this.bleedY = bleedY;
        this.flagForChangeInRelatedParameters = 1;
        return this;
    }

    @Override
    public int getTypeGrid() {
        return typeGrid;
    }

    public TrianglifyView setTypeGrid(int typeGrid) {
        this.typeGrid = typeGrid;
        this.flagForChangeInRelatedParameters = 1;
        return this;
    }

    @Override
    public int getVariance() {
        return variance;
    }

    public TrianglifyView setVariance(int variance) {
        this.variance = variance;
        this.flagForChangeInRelatedParameters = 1;
        return this;
    }

    @Override
    public int getPaletteNumber() {
        return paletteNumber;
    }

    public TrianglifyView setPaletteNumber(int paletteNumber) {
        this.paletteNumber = paletteNumber;
        return this;
    }

    @Override
    public Triangulation getTriangulation() {
        return triangulation;
    }

    public void setTriangulation(Triangulation triangulation) {
        this.triangulation = triangulation;
    }

    @Override
    public boolean isFillTriangle() {
        return fillTriangle;
    }

    public TrianglifyView setFillTriangle(boolean fillTriangle) {
        this.fillTriangle = fillTriangle;
        if(this.flagForChangeInRelatedParameters != 1 && this.flagForChangeInRelatedParameters != 2){
            this.flagForChangeInRelatedParameters = 0;
        }
        return this;
    }

    @Override
    public boolean isDrawStrokeEnabled() {
        return drawStroke;
    }

    public TrianglifyView setDrawStrokeEnabled(boolean drawStroke) {
        this.drawStroke = drawStroke;
        if(this.flagForChangeInRelatedParameters != 1 && this.flagForChangeInRelatedParameters != 2){
            this.flagForChangeInRelatedParameters = 0;
        }
        return this;
    }

    @Override
    public boolean isRandomColoringEnabled() {
        return randomColoring;
    }

    public TrianglifyView setRandomColoring(boolean randomColoring) {
        this.randomColoring = randomColoring;
        if(this.flagForChangeInRelatedParameters != 1){
            this.flagForChangeInRelatedParameters = 2;
        }
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(flagForChangeInRelatedParameters == 0) {
            plotOnCanvas(canvas);
        }else if(flagForChangeInRelatedParameters == 2){
            generateNewColoredSoupAndPlot(canvas);
        }else if(flagForChangeInRelatedParameters == 1){
            generateAndPlot(canvas);
        }
    }

    void generateAndPlot(Canvas canvas) {
        generate();
        plotOnCanvas(canvas);
    }

    void generateNewColoredSoupAndPlot(Canvas canvas){
        generateColoredSoup();
        plotOnCanvas(canvas);
    }

    void generate() {
        this.triangulation = presenter.getSoup(0);
        this.flagForChangeInRelatedParameters = -1;
    }

    void generateColoredSoup(){
        this.triangulation = presenter.getSoup(1);
        this.flagForChangeInRelatedParameters = -1;
    }

    void plotOnCanvas(Canvas canvas) {
        for (int i = 0; i < triangulation.getTriangleList().size(); i++){
            drawTriangle(canvas, triangulation.getTriangleList().get(i));
        }
    }

    /**
     * Draws triangle on the canvas object passed using the parameters of current view instance
     * @param canvas Canvas to paint on
     * @param triangle2D Triangle to draw on canvas
     */
    public void drawTriangle(Canvas canvas, Triangle2D triangle2D) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int color = triangle2D.getColor();

        /*
         * Right shifts number by 8 bits (2 hex for alpha) since color received in triangle2D
         * is without alpha channel.
         */
        color <<= 8;
        color += 255;

        paint.setColor(color);
        paint.setStrokeWidth(4);
        if (isFillTriangle() && isDrawStrokeEnabled()) {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        } else if (isFillTriangle()) {
            paint.setStyle(Paint.Style.FILL);
        } else if (isDrawStrokeEnabled()) {
            paint.setStyle(Paint.Style.STROKE);
        } else {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
        paint.setAntiAlias(true);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        path.moveTo(triangle2D.a.x - bleedX, triangle2D.a.y - bleedY);
        path.lineTo(triangle2D.b.x - bleedX, triangle2D.b.y - bleedY);
        path.lineTo(triangle2D.c.x - bleedX, triangle2D.c.y - bleedY);
        path.lineTo(triangle2D.a.x - bleedX, triangle2D.a.y - bleedY);
        path.close();

        canvas.drawPath(path, paint);
    }
}