package tw.edu.ntub.imd.plearnet.service.impl;

import net.bytebuddy.dynamic.DynamicType;
import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.plearnet.bean.TopicBean;
import tw.edu.ntub.imd.plearnet.databaseconfig.dao.MessageDAO;
import tw.edu.ntub.imd.plearnet.databaseconfig.dao.TopicDAO;
import tw.edu.ntub.imd.plearnet.databaseconfig.entity.Message;
import tw.edu.ntub.imd.plearnet.databaseconfig.entity.Topic;
import tw.edu.ntub.imd.plearnet.service.TopicService;
import tw.edu.ntub.imd.plearnet.service.transformer.TopicTransformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class TopicServiceImpl extends BaseServiceImpl<TopicBean, Topic, String> implements TopicService {
    private final TopicDAO topicDAO;
    private final TopicTransformer transformer;
    private final MessageDAO messageDAO;

    public TopicServiceImpl(TopicDAO topicDAO,TopicTransformer transformer, MessageDAO messageDAO){
        super(topicDAO, transformer);
        this.topicDAO = topicDAO;
        this.transformer = transformer;
        this.messageDAO = messageDAO;
    }

    @Override
    public TopicBean save(TopicBean topicBean){
        Topic topic = topicDAO.save(transformer.transferToEntity(topicBean));
        return transformer.transferToBean(topic);
    }

    @Override
    public Optional<TopicBean> getById(Integer id) {
        Optional<Topic> optional = topicDAO.findById(id);
        if (optional.isPresent()) {
            Topic topic = optional.get();
            TopicBean topicBean = transformer.transferToBean(topic);
            return Optional.of(topicBean);
        } else{
            return Optional.empty();
        }
    }

    @Override
    public List<TopicBean> searchMessageByTopicID(Integer topicID){
        List<TopicBean> topicBeanList = new ArrayList<>();

        for (Message message : messageDAO.findByTopicId(topicID)){
            TopicBean topicBean = new TopicBean();

            topicBean.setMessageId(message.getId());
            topicBean.setUserId(message.getUserId());
            topicBean.setMessageContent(message.getContent());
            topicBeanList.add(topicBean);
        }
        return topicBeanList;
    }

    @Override
    public List<TopicBean> searchAll(Integer tag){
        return CollectionUtils.map(topicDAO.findByTagId(tag),transformer :: transferToBean);
    }
}
