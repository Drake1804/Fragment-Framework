package com.webssa.examples.fragmframework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.webssa.examples.fragmframework.fragments.MyFragment;

public class MainActivity extends AppCompatActivity implements MyFragment.OnNavigationListener {


    FragmentDisplayingManager mDisplManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDisplManager = new FragmentDisplayingManager.Builder(getSupportFragmentManager()).containerId(R.id.container).setFragmentsNumber(4)
                .forwardAnimations(R.anim.fly_from_right, R.anim.fly_to_left)
                .backwardAnimations(R.anim.fly_from_left, R.anim.fly_to_right)
                .setFragmentCreator(new FragmentDisplayingManager.FragmentCreator(){
            @Override
            public Fragment onCreateFragmentForIndex(int index) {
                return createFragmentByIndex(index);
            }
        }).create();

        mDisplManager.switchFromTo(-1, 0);

    }


    @Override
    public void onMovePrevious(int currentFragmentId) {
        if (currentFragmentId > 0) {
            mDisplManager.switchFromTo(currentFragmentId, currentFragmentId-1);
            //moveFromToFragment(fragments[currentFragmentId], currentFragmentId - 1);
        }
    }

    @Override
    public void onMoveNext(int currentFragmentId) {
        if (currentFragmentId < mDisplManager.getFragmentsNumber() - 1) {
            mDisplManager.switchFromTo(currentFragmentId, currentFragmentId + 1);
            //moveFromToFragment(fragments[currentFragmentId], currentFragmentId + 1);
        }
    }



    private MyFragment createFragmentByIndex(int index) {
        MyFragment.Builder builder = new MyFragment.Builder().setId(index);
        switch(index) {
            case 0:
                return builder.setTitle("Fragment1").setContent("dfudg fidfgidsfaiduf gaifgaidf")
                        .setBgColorResId(R.color.bg1).setPositionType(MyFragment.PositionType.POSITION_START).create();
            case 1:
                return builder.setTitle("Fragment2").setContent("fksdf 3gfiuegf 7egf ieagf sjdgfjsdfk sdhkdsgf iudsgfkjadbfja fewrw ")
                        .setBgColorResId(R.color.bg2).setPositionType(MyFragment.PositionType.POSITION_MIDDLE).create();
            case 2:
                return builder.setTitle("Fragment3").setContent("jhtjghur 3rghwvhfd ueru4ikr hgel")
                        .setBgColorResId(R.color.bg2).setPositionType(MyFragment.PositionType.POSITION_MIDDLE).create();
            case 3:
                return builder.setTitle("Fragment4").setContent("ahi5ng5rut  uhknr74nt gijr tu4hgeydirj3u")
                        .setBgColorResId(R.color.bg3).setPositionType(MyFragment.PositionType.POSITION_END).create();
            default:
                throw new IllegalArgumentException("Index not supported - "+index);
        }
    }
}
