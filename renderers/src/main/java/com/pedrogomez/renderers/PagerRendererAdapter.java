/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pedrogomez.renderers;

import java.util.Collection;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrogomez.renderers.exception.NullRendererBuiltException;

/**
 * Adaptation of Renderes API to PagerAdapter
 */
public class PagerRendererAdapter<T> extends PagerAdapter {

  private final RendererBuilder<T> rendererBuilder;
  private AdapteeCollection<T> collection;

  public PagerRendererAdapter(RendererBuilder<T> rendererBuilder, AdapteeCollection<T> collection) {
    this.rendererBuilder = rendererBuilder;
    this.collection = collection;
  }

  @Override public int getCount() {
    return collection.size();
  }

  protected AdapteeCollection<T> getCollection() {
    return collection;
  }

  public void add(T element) {
    collection.add(element);
  }

  public void addAll(Collection<? extends T> elements) {
    collection.addAll(elements);
  }

  public void remove(T element) {
    collection.remove(element);
  }

  public void removeAll(Collection<? extends T> elements) {
    collection.removeAll(elements);
  }

  public void clear() {
    collection.clear();
  }

  @Override public View instantiateItem(ViewGroup container, int position) {
    T content = getItem(position);
    rendererBuilder.withContent(content);
    rendererBuilder.withParent(container);
    rendererBuilder.withLayoutInflater(LayoutInflater.from(container.getContext()));
    Renderer<T> renderer = rendererBuilder.build();
    if (renderer == null) {
      throw new NullRendererBuiltException("RendererBuilder have to return a not null Renderer");
    }
    updateRendererExtraValues(content, renderer, position);
    renderer.render();
    renderer.getRootView().setTag(content);
    container.addView(renderer.getRootView());
    return renderer.getRootView();
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view.equals(object);
  }

  protected void updateRendererExtraValues(T content, Renderer<T> renderer, int position) {

  }

  private T getItem(int position) {
    return collection.get(position);
  }
}
