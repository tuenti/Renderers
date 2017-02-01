package com.pedrogomez.renderers;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrogomez.renderers.exception.NullRendererBuiltException;

@Config(emulateSdk = 16) @RunWith(RobolectricTestRunner.class) public class PagerRendererAdapterTest {

  private static final int ANY_SIZE = 11;
  private static final int ANY_POSITION = 2;
  private static final Object ANY_OBJECT = new Object();
  private static final Collection<Object> ANY_OBJECT_COLLECTION = new LinkedList<>();

  private PagerRendererAdapter<Object> adapter;

  @Mock private RendererBuilder mockedRendererBuilder;
  @Mock private AdapteeCollection<Object> mockedCollection;
  @Mock private View mockedConvertView;
  @Mock private ViewGroup mockedParent;
  @Mock private ObjectRenderer mockedRenderer;
  @Mock private View mockedView;

  @Before public void setUp() throws Exception {
    initializeMocks();
    initializeRVRendererAdapter();
  }

  @Test public void shouldReturnTheAdapteeCollection() {
    assertEquals(mockedCollection, adapter.getCollection());
  }

  @Test public void shouldReturnCollectionSizeOnGetCount() {
    when(mockedCollection.size()).thenReturn(ANY_SIZE);
    assertEquals(ANY_SIZE, adapter.getCount());
  }

  @Test public void shouldReturnItemAtCollectionPositionOnGetItem() {
    when(mockedCollection.get(ANY_POSITION)).thenReturn(ANY_OBJECT);
  }

  @Test public void shouldBuildRendererUsingAllNeededDependencies() {
    when(mockedCollection.get(ANY_POSITION)).thenReturn(ANY_OBJECT);
    when(mockedRenderer.getRootView()).thenReturn(mockedView);
    when(mockedRendererBuilder.build()).thenReturn(mockedRenderer);

    adapter.instantiateItem(mockedParent, ANY_POSITION);

    verify(mockedRendererBuilder).withContent(ANY_OBJECT);
    verify(mockedRendererBuilder).withParent(mockedParent);
    verify(mockedRendererBuilder).withLayoutInflater((LayoutInflater) notNull());
  }

  @Test public void shouldAddElementToAdapteeCollection() {
    adapter.add(ANY_OBJECT);

    verify(mockedCollection).add(ANY_OBJECT);
  }

  @Test public void shouldAddAllElementsToAdapteeCollection() {
    adapter.addAll(ANY_OBJECT_COLLECTION);

    verify(mockedCollection).addAll(ANY_OBJECT_COLLECTION);
  }

  @Test public void shouldRemoveElementFromAdapteeCollection() {
    adapter.remove(ANY_OBJECT);

    verify(mockedCollection).remove(ANY_OBJECT);
  }

  @Test public void shouldRemoveAllElementsFromAdapteeCollection() {
    adapter.removeAll(ANY_OBJECT_COLLECTION);

    verify(mockedCollection).removeAll(ANY_OBJECT_COLLECTION);
  }

  @Test public void shouldClearElementsFromAdapteeCollection() {
    adapter.clear();

    verify(mockedCollection).clear();
  }

  @Test public void shouldSetAdapteeCollection() throws Exception {
    RVRendererAdapter<Object> adapter = new RVRendererAdapter<Object>(mockedRendererBuilder);

    adapter.setCollection(mockedCollection);

    assertEquals(mockedCollection, adapter.getCollection());
  }

  @Test public void shouldBeEmptyWhenItsCreatedWithJustARendererBuilder() {
    RVRendererAdapter<Object> adapter = new RVRendererAdapter<Object>(mockedRendererBuilder);

    assertEquals(0, adapter.getItemCount());
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionWhenSetNullCollection() {
    RVRendererAdapter<Object> adapter = new RVRendererAdapter<Object>(mockedRendererBuilder);

    adapter.setCollection(null);
  }

  @Test(expected = NullRendererBuiltException.class)
  public void shouldThrowNullRendererBuiltException() {
    adapter.instantiateItem(mockedParent, ANY_POSITION);
  }

  private void initializeMocks() {
    MockitoAnnotations.initMocks(this);
    when(mockedParent.getContext()).thenReturn(Robolectric.application);
  }

  private void initializeRVRendererAdapter() {
    adapter = new PagerRendererAdapter<>(mockedRendererBuilder, mockedCollection);
    adapter = spy(adapter);
  }
}
