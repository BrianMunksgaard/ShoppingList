package org.projects.shoppinglist.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The product class is used to represent an item
 * in a shopping bag.
 */
public class Product implements Parcelable {

    /*
     * Product name.
     */
    private String name;

    /*
     * Product quantity.
     */
    private int quantity​;

    /**
     * Default empty constructor.
     */
    public Product() {}

    /**
     * Product constructor used to set product name and quantity.
     */
    public Product(String name, int quantity​)
    {
        this.name = name;
        this.quantity​ = quantity​;
    }

    /**
     * This implementation returns both product name and quantity.
     */
    @Override
    public String toString() {
        return name + " " + quantity​;
    }

    /**
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write product data to parcel.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity​);
    }

    /**
     * CREATOR used by the Parcelable interface.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    /*
     * Create product from Parcel.
     */
    private Product(Parcel in) {
        name = in.readString();
        quantity​ = in.readInt();
    }
}
