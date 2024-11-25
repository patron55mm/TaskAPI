package ru.patron55mm.task.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.patron55mm.task.entity.task.Task;
import ru.patron55mm.task.repository.task.TaskCommentRepository;
import ru.patron55mm.task.web.response.CommentResponse;
import ru.patron55mm.task.web.response.TaskWithCommentsResponse;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(TaskCommentRepository commentRepository) {
        ModelMapper modelMapper = new ModelMapper();

        // Конвертер для Task -> TaskWithCommentsResponse
        Converter<Task, TaskWithCommentsResponse> taskToDtoConverter = context -> {
            Task task = context.getSource();

            List<CommentResponse> commentResponses = commentRepository.findAllByTask(task).stream()
                    .map(comment -> new CommentResponse(
                            comment.getId(),
                            comment.getComment(),
                            comment.getUser().getEmail(),
                            comment.getCreated()
                    ))
                    .toList();

            return new TaskWithCommentsResponse(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getPriority(),
                    task.getStatus(),
                    task.getExecutor() != null ? task.getExecutor().getEmail() : null,
                    task.getAuthor() != null ? task.getAuthor().getEmail() : null,
                    commentResponses,
                    task.getCreated()
            );
        };

        modelMapper.createTypeMap(Task.class, TaskWithCommentsResponse.class)
                .setConverter(taskToDtoConverter);

        return modelMapper;
    }

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                .setPropertyCondition(Conditions.isNotNull());

        return modelMapper;
    }
}