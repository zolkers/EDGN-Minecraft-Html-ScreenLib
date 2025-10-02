package com.edgn.edml.layout.box;

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