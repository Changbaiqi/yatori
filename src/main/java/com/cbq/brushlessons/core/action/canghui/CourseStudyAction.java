package com.cbq.brushlessons.core.action.canghui;

import com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse.MyCourse;
import com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse.ProgressDetail;
import com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse.VideoRouter;
import com.cbq.brushlessons.core.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CourseStudyAction implements Runnable{
    private User user;

    private MyCourse myCourse;

    //视屏
    private VideoRouter videoRouter;

    //视屏观看记录
    private ProgressDetail progressDetail;

    private boolean newThread;
    public void toStudy(){
        if(newThread){
            new Thread(this).start();
        }else {
            log.info("{}:正在学习课程>>>{}",user.getAccount(),myCourse.getCourse().getTitle());
            study();
            log.info("{}:{}学习完毕！",user.getAccount(),myCourse.getCourse().getTitle());
        }
    }

    public void study(){
        for(int i=0; i < videoRouter.getData().size();++i){
        }
    }
    @Override
    public void run() {

    }
}
