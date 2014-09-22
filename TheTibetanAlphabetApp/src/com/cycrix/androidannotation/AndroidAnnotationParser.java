package com.cycrix.androidannotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class AndroidAnnotationParser {
	
	private static Class[] eventAnnotation = new Class[] {Click.class, ItemClick.class};
	private static Class resourceClass;
	
	public static void parse(Object container, View groupView) throws Exception {
		String resourcePackageName = groupView.getContext().getPackageName() + ".R$id";
		resourceClass = Class.forName(resourcePackageName);
		
		parseInject(container, groupView);
		parseEventBinder(container, groupView);
	}
	
	private static void parseInject(Object container, View groupView) throws Exception {
		
		for (Field field : container.getClass().getDeclaredFields()) {
			
			ViewById anno = field.getAnnotation(ViewById.class);
			
			if (anno == null)
				continue;
			
			int id = anno.id();
			if (id == 0)
				id = resourceClass.getField(field.getName()).getInt(null);
			field.setAccessible(true);
			View v = null;
			try {
				v = groupView.findViewById(id);
				field.set(container, v);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(e.getMessage() + " " + field.getName());
			}
		}
	}
	
	private static void parseEventBinder(final Object container, View groupView) throws Exception {
		
		// Scan all method
		for (final Method method : container.getClass().getDeclaredMethods()) {
			
			// Scan each event type
			for (Class classAnno : eventAnnotation) {
				Annotation anno = method.getAnnotation(classAnno);
				
				// This this event type isn't specified, bypass
				if (anno == null)
					continue;
				
				Object viewObj = null;
				
				if (classAnno == Click.class) {
					int id = ((Click) anno).id();
					viewObj = getView(id, method.getName(), groupView);
					Method clickMethod = viewObj.getClass().getMethod("setOnClickListener", 
							OnClickListener.class);
					method.setAccessible(true);
					Type[] paramArr = method.getGenericParameterTypes();
					if (paramArr.length != 1 || paramArr[0] != View.class)
						throw new IllegalArgumentException("Click method " + method.getName() 
								+ " require only one View parameter");
					clickMethod.invoke(viewObj, new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								method.invoke(container, v);
							} catch (Exception e) { }
						}
					});
				} else if (classAnno == ItemClick.class) {
					int id = ((Click) anno).id();
					viewObj = getView(id, method.getName(), groupView);
					Method clickMethod = viewObj.getClass().getMethod("setOnItemClickListener", 
							OnItemClickListener.class);
					clickMethod.invoke(viewObj, new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							try {
								method.invoke(container, parent, view, position, id);
							} catch (Exception e) { }
						}
					});
				}
			}
		}
	}
	
	private static View getView(int id, String name, View groupView) throws Exception {
		if (id == 0) { 
			id = resourceClass.getField(name).getInt(null);
			return groupView.findViewById(id);
		} else {
			return groupView.findViewById(id);
		}
	}
}