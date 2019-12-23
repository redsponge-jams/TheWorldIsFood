package com.redsponge.foodworld.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.redengine.assets.AssetSpecifier;
import com.redsponge.redengine.screen.AbstractScreen;
import com.redsponge.redengine.screen.components.Mappers;
import com.redsponge.redengine.screen.components.RenderRunnableComponent;
import com.redsponge.redengine.screen.systems.RenderSystem;
import com.redsponge.redengine.utils.GameAccessor;

public class GameScreen extends AbstractScreen {

    public static final float RATIO = 9/16f;
    public static final int WIDTH = 320;
    public static final int HEIGHT = (int) (WIDTH * RATIO);

    private FitViewport guiViewport;
    private RenderSystem renderSystem;

    private GameGUI gui;
    private GameStations stations;

    private InputMultiplexer inputMultiplexer;

    public static final Vector2 mousePos = new Vector2();
    private Stage stage;

    public GameScreen(GameAccessor ga) {
        super(ga);
    }

    @Override
    public void show() {
        super.show();

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(MouseInput.getInstance());

        renderSystem = getEntitySystem(RenderSystem.class);

        stations = new GameStations(batch, shapeRenderer);
        addEntity(stations);

        gui = new GameGUI(batch, shapeRenderer);
        addEntity(gui);

        stage = new Stage(renderSystem.getViewport(), batch);
        inputMultiplexer.addProcessor(stage);

        addEntity(new Planet(batch, shapeRenderer));
    }


    @Override
    public int getScreenWidth() {
        return WIDTH;
    }

    @Override
    public int getScreenHeight() {
        return HEIGHT;
    }

    @Override
    public void tick(float v) {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY());
        renderSystem.getViewport().unproject(mousePos);

        tickEntities(v);
        updateEngine(v);
        stage.act(v);
        stage.draw();

        MouseInput.getInstance().allOff();
    }

    @Override
    public void render() {
    }

    @Override
    public void reSize(int width, int height) {
        renderSystem.resize(width, height);
    }

    @Override
    public Class<? extends AssetSpecifier> getAssetSpecsType() {
        return GameAssets.class;
    }

    public void setStation(int index) {
        stations.setSelectedIndex(index);
    }

    public Stage getStage() {
        return stage;
    }
}