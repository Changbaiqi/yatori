package com.cbq.brushlessons.core.aggregate;

import com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse.Course;
import com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse.MyCourse;
import com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse.MyCourseData;
import com.cbq.brushlessons.core.action.enaea.entity.underwayproject.ResultList;
import com.cbq.brushlessons.core.action.enaea.entity.underwayproject.UnderwayProjectRquest;
import com.cbq.brushlessons.core.action.yinghua.CourseAction;
import com.cbq.brushlessons.core.action.yinghua.CourseStudyAction;
import com.cbq.brushlessons.core.action.yinghua.entity.allcourse.CourseInform;
import com.cbq.brushlessons.core.action.yinghua.entity.allcourse.CourseRequest;
import com.cbq.brushlessons.core.entity.User;
import lombok.Data;


@Data
public class YatoriLogin {
    private User user;
    public YatoriLogin(User user) {
        this.user = user;
    }
}
