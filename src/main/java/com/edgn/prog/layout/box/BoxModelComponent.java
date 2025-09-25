package com.edgn.prog.layout.box;

import com.edgn.prog.layout.spacing.Margin;
import com.edgn.prog.layout.spacing.Padding;

public interface BoxModelComponent {
    BoxModel getBoxModel();
    void setBoxModel(BoxModel boxModel);
    
    default Margin getMargin() {
        return getBoxModel().margin();
    }
    
    default Padding getPadding() {
        return getBoxModel().padding();
    }
    
    default void setMargin(Margin margin) {
        setBoxModel(getBoxModel().withMargin(margin));
    }
    
    default void setPadding(Padding padding) {
        setBoxModel(getBoxModel().withPadding(padding));
    }
}