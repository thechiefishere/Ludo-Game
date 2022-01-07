package com.jbaba.ludo.concreteclasses;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jbaba.ludo.abstractclasses.AbsSprite;

import java.util.ArrayList;

public class Road extends AbsSprite
{
    private RoadSegment[] roadSeg;

    public Road()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        roadSeg = new RoadSegment[72];
        setVisible(true);
        setActive(false);


        int x = 0;
        int y = 0;
        int inc = 0;

        for(int i = 0; i < 4; i++)
        {
            if(i == 0)
            {
                x = 5 * MyConsts.roadSegmentLength + MyConsts.boardStartingX;
                //x = MyConsts.houseLength  + MyConsts.boardStartingX - MyConsts.roadSegmentLength;
                y = 2 * MyConsts.roadSegmentLength + MyConsts.houseLength;

                for(int j = 0; j < 18; j++)
                {
                    roadSeg[inc] = new RoadSegment();
                    if(j < 6)
                    {
                        roadSeg[inc].setX(x);
                        roadSeg[inc].setY(y);
                        roadSeg[inc].setSegmentNum(inc);

                        x -= MyConsts.roadSegmentLength;
                        if(j == 5)
                        {
                            x = MyConsts.boardStartingX;
                            y -= MyConsts.roadSegmentLength;
                        }
                    }
                    else if(j < 12)
                    {
                        roadSeg[inc].setX(x);
                        roadSeg[inc].setY(y);
                        roadSeg[inc].setSegmentNum(inc);

                        x += MyConsts.roadSegmentLength;
                        if(j == 11)
                        {
                            x = MyConsts.boardStartingX;
                            y -= MyConsts.roadSegmentLength;
                        }
                    }
                    else
                    {
                        roadSeg[inc].setX(x);
                        roadSeg[inc].setY(y);
                        roadSeg[inc].setSegmentNum(inc);

                        x += MyConsts.roadSegmentLength;
                    }
                    inc++;
                }
            }
            else if(i == 1)
            {
                x = MyConsts.boardStartingX + MyConsts.houseLength;
                y = 5 * MyConsts.roadSegmentLength + MyConsts.boardStartingY;

                for(int j = 0; j < 18; j++)
                {
                    roadSeg[inc] = new RoadSegment();
                    if(j < 6)
                    {
                        roadSeg[inc].setX(x);
                        roadSeg[inc].setY(y);
                        roadSeg[inc].setSegmentNum(inc);

                        y -= MyConsts.roadSegmentLength;
                        if(j == 5)
                        {
                            y = MyConsts.boardStartingY;
                            x += MyConsts.roadSegmentLength;
                        }
                    }
                    else if(j < 12)
                    {
                        roadSeg[inc].setX(x);
                        roadSeg[inc].setY(y);
                        roadSeg[inc].setSegmentNum(inc);

                        y += MyConsts.roadSegmentLength;
                        if(j == 11)
                        {
                            y = MyConsts.boardStartingY;
                            x += MyConsts.roadSegmentLength;
                        }
                    }
                    else
                    {
                        roadSeg[inc].setX(x);
                        roadSeg[inc].setY(y);
                        roadSeg[inc].setSegmentNum(inc);

                        y += MyConsts.roadSegmentLength;
                    }
                    inc++;
                }
            }
            else if(i == 2)
            {
                x = MyConsts.boardEndingX - (6 * MyConsts.roadSegmentLength);
                y = MyConsts.boardStartingY + MyConsts.houseLength;

                for(int j = 0; j < 18; j++)
                {
                    roadSeg[inc] = new RoadSegment();
                    if(j < 6)
                    {
                        roadSeg[inc].setX(x);
                        roadSeg[inc].setY(y);
                        roadSeg[inc].setSegmentNum(inc);

                        x += MyConsts.roadSegmentLength;
                        if(j == 5)
                        {
                            x = MyConsts.boardEndingX - MyConsts.roadSegmentLength;
                            y += MyConsts.roadSegmentLength;
                        }
                    }
                    else if(j < 12)
                    {
                        roadSeg[inc].setX(x);
                        roadSeg[inc].setY(y);
                        roadSeg[inc].setSegmentNum(inc);

                        x -= MyConsts.roadSegmentLength;
                        if(j == 11)
                        {
                            x = MyConsts.boardEndingX - MyConsts.roadSegmentLength;
                            y += MyConsts.roadSegmentLength;
                        }
                    }
                    else
                    {
                        roadSeg[inc].setX(x);
                        roadSeg[inc].setY(y);
                        roadSeg[inc].setSegmentNum(inc);

                        x -= MyConsts.roadSegmentLength;
                    }
                    inc++;
                }
            }
            else if(i == 3)
            {
                x = 2 * MyConsts.roadSegmentLength + MyConsts.boardStartingX + MyConsts.houseLength;
                y = MyConsts.boardHeight - (6 * MyConsts.roadSegmentLength);

                for(int j = 0; j < 18; j++)
                {
                    roadSeg[inc] = new RoadSegment();
                    if(j < 6)
                    {
                        roadSeg[inc].setX(x);
                        roadSeg[inc].setY(y);
                        roadSeg[inc].setSegmentNum(inc);

                        y += MyConsts.roadSegmentLength;
                        if(j == 5)
                        {
                            x -= MyConsts.roadSegmentLength;
                            y = MyConsts.boardHeight - MyConsts.roadSegmentLength;
                        }
                    }
                    else if(j < 12)
                    {
                        roadSeg[inc].setX(x);
                        roadSeg[inc].setY(y);
                        roadSeg[inc].setSegmentNum(inc);

                        y -= MyConsts.roadSegmentLength;
                        if(j == 11)
                        {
                            x -= MyConsts.roadSegmentLength;
                            y = MyConsts.boardHeight - MyConsts.roadSegmentLength;
                        }
                    }
                    else
                    {
                        roadSeg[inc].setX(x);
                        roadSeg[inc].setY(y);
                        roadSeg[inc].setSegmentNum(inc);

                        y -= MyConsts.roadSegmentLength;
                    }
                    inc++;
                }
            }

        }
    }

    @Override
    public void updateSprite()
    {

    }

    @Override
    public void drawSprite(Canvas canvas, Paint paint)
    {
        for(int i = 0; i < roadSeg.length; i++)
            if(roadSeg[i].isVisible())
                roadSeg[i].drawSprite(canvas, paint);
    }

    public RoadSegment[] getRoadSeg() {
        return roadSeg;
    }

    public void setRoadSeg(RoadSegment[] roadSeg) {
        this.roadSeg = roadSeg;
    }
}
