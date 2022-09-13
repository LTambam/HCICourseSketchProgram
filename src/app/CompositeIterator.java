package app;

import java.util.*;

public class CompositeIterator implements Iterator<SketchComponent>{
    Stack<Iterator<SketchComponent>> stack = new Stack<Iterator<SketchComponent>>();
    public CompositeIterator(Iterator<SketchComponent> iterator){
        stack.push(iterator);
    }

    public SketchComponent next(){
        if (hasNext()) {
			Iterator<SketchComponent> iterator = stack.peek();
			SketchComponent component = iterator.next();
			stack.push(component.createIterator());
			return component;
		} else {
			return null;
		}
    }
    public boolean hasNext() {
		if (stack.empty()) {
			return false;
		} else {
			Iterator<SketchComponent> iterator = stack.peek();
			if (!iterator.hasNext()) {
				stack.pop();
				return hasNext();
			} else {
				return true;
			}
		}
	}
}
