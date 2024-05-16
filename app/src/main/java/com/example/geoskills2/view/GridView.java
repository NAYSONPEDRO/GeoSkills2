package com.example.geoskills2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GridView extends View {
    private int lines;
    private int columns;
    private Paint paint;
    public GridView(Context context, int lines, int columns) {
        super(context);
        this.lines = lines;
        this.columns = columns;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        // Desenha as linhas horizontais da grade
        for (int i = 0; i < lines; i++) {
            float y = i * height / lines;
            canvas.drawLine(0, y, width, y, paint);
        }

        // Desenha as linhas verticais da grade
        for (int i = 0; i < columns; i++) {
            float x = i * width / columns;
            canvas.drawLine(x, 0, x, height, paint);
        }
    }
}
