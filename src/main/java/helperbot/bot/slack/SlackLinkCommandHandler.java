package helperbot.bot.slack;

import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import helperbot.bot.command.CommandParsedResult;
import helperbot.bot.command.link.LinkCommandType;
import helperbot.bot.command.link.LinkEntityCommand;
import helperbot.entity.LinkEntity;
import helperbot.entity.TagEntity;
import helperbot.service.LinkService;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class SlackLinkCommandHandler implements SlackCommandHandler {

    private LinkService linkService;
    private ObjectMapper objectMapper;
    private String helpMessage;

    public SlackLinkCommandHandler(LinkService linkService) {
        Objects.requireNonNull(linkService, "linkService must be not null");
        this.linkService = linkService;
        this.objectMapper = new ObjectMapper();
        this.helpMessage = new StringBuilder("## !link command usages\n")
            .append("!link add -t title -d description -h href -tag #tag1#tag2 , where \"__\" replaced \" \"\n")
            .append("\te.g) !link add -t spring -d spring__docs -h https://spring.io/docs -tag #spring#docs\n")
            .append("!link update -t title -d description -h href -tag #tag1#tag2\n")
            .append("\te.g) !link update -i 1 -tag #spring#doc\n")
            .append("!link query -tag tag01     , find all if no args\n")
            .append("\te.g) !link query -tag #spring  ||  !link query spring\n")
            .append("!link delete -i id\n")
            .append("\te.g) !link delete -i 1\n")
            .append("!link backup [pretty]  , where pretty is optional\n")
            .append("\te.g) !link backup pretty\n")
            .toString();
    }

    @Override
    public void handleCommand(SlackMessageSender messageSender, CommandParsedResult parsedResult) {
        try {
            String[] totalArgs = parsedResult.getArgs();
            if (totalArgs.length == 0) {
                throw new Exception("Invalid command. try !link help");
            }

            LinkCommandType linkCommandType = LinkCommandType.getType(totalArgs[0]);
            String[] args = new String[totalArgs.length - 1];
            System.arraycopy(totalArgs, 1, args, 0, totalArgs.length - 1);

            if (logger.isDebugEnabled()) {
                logger.debug("Handle slack link command. type : {} / args : {}"
                    , linkCommandType, Arrays.toString(args));
            }

            switch (linkCommandType) {
                case ADD:
                    handleAddCommand(messageSender, args);
                    break;
                case UPDATE:
                    handleUpdateCommand(messageSender, args);
                    break;
                case DELETE:
                    handleDeleteCommand(messageSender, args);
                    break;
                case QUERY:
                    handleQueryCommand(messageSender, args);
                    break;
                case BACKUP:
                    handleBackupCommand(messageSender, args);
                    break;
                case HELP:
                case UNKNOWN:
                default:
                    messageSender.send(helpMessage);
            }
        } catch (Exception e) {
            logger.warn("Exception occur while handleCommand", e);
            String errorMessage = e.getMessage() == null ? "Unknown reason" : e.getMessage();
            messageSender.send(errorMessage);
        }
    }

    /**
     * Handle link add command
     */
    private void handleAddCommand(SlackMessageSender messageSender, String[] args) throws Exception {
        LinkEntityCommand linkEntityCommand = parseLinkEntityCommand(args);
        if (logger.isDebugEnabled()) {
            logger.debug("## [Link add command] " + linkEntityCommand);
        }

        LinkEntity saved = linkService.saveLink(linkEntityCommand);

        String message = String.format("Success to save link. title %s > id : %d", saved.getTitle(), saved.getId());
        messageSender.send(message);
    }

    /**
     * Handle link update command
     */
    private void handleUpdateCommand(SlackMessageSender messageSender, String[] args) throws Exception {
        LinkEntityCommand linkEntityCommand = parseLinkEntityCommand(args);
        if (logger.isDebugEnabled()) {
            logger.debug("## [Link update command] " + linkEntityCommand);
        }

        LinkEntity updated = linkService.updateLink(linkEntityCommand);
        String message = String.format("Success to update link. title %s > id : %d"
            , updated.getTitle(), updated.getId());
        messageSender.send(message);
    }

    private void handleDeleteCommand(SlackMessageSender messageSender, String[] args) throws Exception {
        LinkEntityCommand linkEntityCommand = parseLinkEntityCommand(args);
        if (logger.isDebugEnabled()) {
            logger.debug("## [Link delete command] " + linkEntityCommand);
        }

        if (linkEntityCommand.getId() == null) {
            throw new Exception("Invalid args. delete command usage : !link delete -i id");
        }

        linkService.deleteLink(linkEntityCommand.getId());
        messageSender.send("Success to delete " + linkEntityCommand.getId());
    }

    private void handleQueryCommand(SlackMessageSender messageSender, String[] args) throws Exception {
        // change !link query tagName > !link query -tag tagName
        if (args != null && args.length > 0 && args[0].charAt(0) != '-') {
            args = new String[]{"-tag", args[0]};
        }

        LinkEntityCommand linkEntityCommand = parseLinkEntityCommand(args);
        if (logger.isDebugEnabled()) {
            logger.debug("## [Link query command] " + linkEntityCommand);
        }

        String tagName = CollectionUtils.isEmpty(linkEntityCommand.getTagValues())
            ? null : linkEntityCommand.getTagValues().get(0);

        List<LinkEntity> links = linkService.queryByTagName(tagName);

        final int entityStringLength = 50;
        final String delimiter = "   ";
        StringBuilder message = new StringBuilder(entityStringLength * links.size());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (LinkEntity linkEntity : links) {
            message.append("[Id.title] : ")
                .append(linkEntity.getId()).append(".").append(linkEntity.getTitle()).append(delimiter)
                .append("[Description] : ").append(linkEntity.getDescription()).append(delimiter)
                .append("[Link] : ").append(linkEntity.getHref()).append(delimiter)
                .append("[Reg time] : ").append(linkEntity.getRegisterDateTime().format(formatter)).append(delimiter);

            message.append("[Tags] : ");
            for (TagEntity tagEntity : linkEntity.getTags()) {
                message.append("#").append(tagEntity.getName()).append("  ");
            }
            message.append("\n\n");
        }
        /*for (LinkEntity linkEntity : links) {
            message.append("[").append(linkEntity.getId()).append(". ")
                .append(linkEntity.getTitle()).append("]").append(delimiter)
                .append("[").append(linkEntity.getDescription()).append("]").append(delimiter)
                .append("[").append(linkEntity.getHref()).append("]").append(delimiter)
                .append("[").append(linkEntity.getRegisterDateTime().format(formatter)).append("]").append(delimiter);

            message.append("[ ");
            for (TagEntity tagEntity : linkEntity.getTags()) {
                message.append("#").append(tagEntity.getName()).append("  ");
            }
            message.append("]\n\n");
        }*/

        messageSender.send(message.toString());
    }

    private void handleBackupCommand(SlackMessageSender messageSender, String[] args) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("## [Link backup command] " + Arrays.toString(args));
        }

        boolean pretty = args != null && args.length > 0 && args[0].equalsIgnoreCase("pretty");
        ObjectWriter writer = pretty ? objectMapper.writerWithDefaultPrettyPrinter() : objectMapper.writer();
        messageSender.send(writer.writeValueAsString(linkService.queryByTagName(null)));
    }

    private LinkEntityCommand parseLinkEntityCommand(String[] args) {
        LinkEntityCommand linkEntityCommand = new LinkEntityCommand();
        JCommander.newBuilder()
            .addObject(linkEntityCommand)
            .build()
            .parse(args);

        return linkEntityCommand;
    }
}
