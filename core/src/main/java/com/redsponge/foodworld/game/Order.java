package com.redsponge.foodworld.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.redsponge.foodworld.ReverseInterpolation;
import com.redsponge.redengine.screen.components.RenderRunnableComponent;
import com.redsponge.redengine.screen.components.TextureComponent;
import com.redsponge.redengine.screen.entity.ScreenEntity;
import com.redsponge.redengine.utils.GeneralUtils;

public class Order extends ScreenEntity {

    private Actor actor;

    private TextureRegion paper;
    private TextureRegion clipper;

    private Planet p;

    private int index;

    private TextureComponent tex;
    private Color clipperColour;

    public static final Color[] CLIPPER_COLOURS = {
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.GOLD,
            Color.YELLOW,
            Color.SKY,
            Color.SCARLET
    };
    private boolean dragged;

    public Order(SpriteBatch batch, ShapeRenderer sr, int human, int plants, boolean frozen, boolean volcanic, boolean water, int index) {
        super(batch, sr);
        this.index = index;
        p = new Planet();
        p.setHumanLevel(human);
        p.setSeedsLevel(plants);
        p.setFrozen(frozen);
        p.setVolcanic(volcanic);
        p.setHasWater(water);
        p.setDraggable(false);

        rollColour();
    }

    public void rollColour() {
        clipperColour = GeneralUtils.randomItem(CLIPPER_COLOURS);
    }

    @Override
    public void added() {
        pos.set((index) * 38 + 44, 180 - 50, 20 + index);
        size.set(32, 48);
        render.setScaleX(0.75f).setScaleY(0.75f);
        actor = new Actor();
        actor.setPosition(pos.getX(), pos.getY(), Align.center);
        actor.setSize(size.getX(), size.getY());
//        actor.setScale(0.75f);
        actor.setOrigin(Align.center);

        ((GameScreen)screen).getStage().addActor(actor);
        actor.addListener(new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                actor.moveBy(x - actor.getWidth() / 2, y - actor.getHeight() / 2);
                dragged = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                actor.moveBy(x - actor.getWidth() / 2, y - actor.getHeight() / 2);
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                actor.addAction(Actions.sequence(Actions.moveTo(actor.getX() < 35 ? 35 : actor.getX() > 280 ? 280 : actor.getX(), 180 - 50, 0.2f, Interpolation.circleOut), Actions.run(() -> {
                    dragged = false;
                })));
            }

            //            @Override
//            public void drag(InputEvent event, float x, float y, int pointer) {
//                actor.moveBy(x - actor.getWidth() / 2, y - actor.getHeight() / 2);
//                dragged = true;
//            }
//
//            @Override
//            public void dragStop(InputEvent event, float x, float y, int pointer) {
//                super.dragStop(event, x, y, pointer);
//                actor.addAction(Actions.moveTo(actor.getX() < 35 ? 35 : actor.getX() > 280 ? 280 : actor.getX(), 180 - 40, 0.2f, Interpolation.circleOut));
//                dragged = false;
//            }
        });
    }

    @Override
    public void additionalTick(float delta) {
        super.additionalTick(delta);
        //pos.set((int) actor.getX(), (int) actor.getY());
        p.setPosition(actor.getX(), actor.getY());
    }

    @Override
    public void loadAssets() {
        p.loadAssets(assets);
        p.setScale(0.5f);
//        p.setPosition((int) (pos.getX() - 4), (int) (pos.getY()));

        paper = assets.getTextureRegion("orderPaper");
        clipper = assets.getTextureRegion("orderClip");

        add(new RenderRunnableComponent(() -> {
            float w = actor.getWidth() * actor.getScaleX();
            float h = actor.getHeight() * actor.getScaleY();
            float x = actor.getX();
            float y = actor.getY();

            batch.setColor(Color.WHITE);
            batch.draw(paper, x, y, w, h);
//            if(!dragged) {
//                batch.setColor(clipperColour);
//                batch.draw(clipper, actor.getX(), actor.getY(), size.getX() / 4f * 3, size.getY() / 4f * 3);
//            }
            batch.setColor(Color.WHITE);
            p.setPosition(actor.getX(), actor.getY());
            p.render(batch);
        }));
    }
}
