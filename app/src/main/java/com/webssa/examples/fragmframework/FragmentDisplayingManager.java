package com.webssa.examples.fragmframework;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by stanislav.perchenko on 10/18/2016.
 */

public class FragmentDisplayingManager {


    private int containerId;
    private int animForwardIn, animForwardOut;
    private int animBackwardIn, animBackwardOut;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;

    private FragmentCreator fragmentCreator;

    private FragmentDisplayingManager(Builder b) {
        this.fragmentManager = b.fragmentManager;
        fragments = new Fragment[b.nFragments];
        this.containerId = b.containerId;
        this.animForwardIn = b.animForwardIn;
        this.animForwardOut = b.animForwardOut;
        this.animBackwardIn = b.animBackwardIn;
        this.animBackwardOut = b.animBackwardOut;
        this.fragmentCreator = b.fragmentCreator;
    }


    private FragmentTransaction fTrans;


    public int getFragmentsNumber() {
        return fragments.length;
    }

    public Fragment getFragmentByIndex(int index) {
        return (index >= 0 && index < fragments.length) ? fragments[index] : null;
    }


    public Fragment switchFromTo(int fromIndex, int toIndex) {
        if (fromIndex >= fragments.length) {
            throw new IndexOutOfBoundsException("from index - "+fromIndex);
        }
        if (toIndex < 0 || toIndex >=fragments.length) {
            throw new IndexOutOfBoundsException("To index - "+toIndex);
        }


        if (fromIndex == toIndex) return fragments[toIndex];



        fTrans = setAnimationsForTransaction(fragmentManager.beginTransaction(), fromIndex, toIndex);

        hideCurrentFragmentByIndex(fromIndex);

        showRequestedFragmentByIndex(toIndex);

        fTrans.commit();

        return fragments[toIndex];
    }

    private FragmentTransaction setAnimationsForTransaction(@NonNull FragmentTransaction ft, int fromIndex, int toIndex) {
        return (fromIndex < toIndex) ? ft.setCustomAnimations(animForwardIn, animForwardOut) : ft.setCustomAnimations(animBackwardIn, animBackwardOut);
    }

    private void hideCurrentFragmentByIndex(int index) {
        if ((index >= 0) && (fragments[index] != null)) {
            fTrans.detach(fragments[index]);
        }
    }

    private void showRequestedFragmentByIndex(int index) {
        if (fragments[index] == null) {
            fragments[index] = fragmentCreator.onCreateFragmentForIndex(index);
            fTrans.add(containerId, fragments[index]);
        } else {
            fTrans.attach(fragments[index]);
        }
    }







    public interface FragmentCreator {
        Fragment onCreateFragmentForIndex(int index);
    }



    public static class Builder {
        private FragmentManager fragmentManager;
        private int animForwardIn, animForwardOut;
        private int animBackwardIn, animBackwardOut;
        private int containerId;
        private int nFragments;
        private FragmentCreator fragmentCreator;

        public Builder(@NonNull FragmentManager fm) {
            fragmentManager = fm;
        }

        public Builder setFragmentsNumber(int nFragments) {
            this.nFragments = nFragments;
            return this;
        }

        public Builder containerId(int containerId) {
            this.containerId = containerId;
            return this;
        }

        public Builder forwardAnimations(int animationInResId, int animationOutResId) {
            animForwardIn = animationInResId;
            animForwardOut = animationOutResId;
            return this;
        }

        public Builder backwardAnimations(int animationInResId, int animationOutResId) {
            animBackwardIn = animationInResId;
            animBackwardOut = animationOutResId;
            return this;
        }


        public Builder setFragmentCreator(FragmentCreator fragmentCreator) {
            this.fragmentCreator = fragmentCreator;
            return this;
        }

        public FragmentDisplayingManager create() {
            //TODO Validate all mandatory fields has been provided!
            return new FragmentDisplayingManager(this);
        }
    }

}
