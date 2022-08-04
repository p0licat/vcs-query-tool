import {
  List,
  ListItem,
  ListItemAvatar,
  Avatar,
  ListItemText,
} from "@mui/material";

import ImageSearchIcon from "@mui/icons-material/ImageSearch";
import NetworkCellIcon from "@mui/icons-material/NetworkCell";

export interface ListUsersElementProps {}

export function ListUsersElement(props: ListUsersElementProps) {
  return (
    <List sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}>
      <ListItem>
        <ListItemAvatar>
          <Avatar>
            <ImageSearchIcon />
          </Avatar>
        </ListItemAvatar>
        <ListItemText primary="Photos" secondary="Jan 9, 2014" />
      </ListItem>
      <ListItem>
        <ListItemAvatar>
          <Avatar>
            <NetworkCellIcon />
          </Avatar>
        </ListItemAvatar>
        <ListItemText primary="Work" secondary="Jan 7, 2014" />
      </ListItem>
      <ListItem>
        <ListItemAvatar>
          <Avatar>
            <NetworkCellIcon />
          </Avatar>
        </ListItemAvatar>
        <ListItemText primary="Vacation" secondary="July 20, 2014" />
      </ListItem>
    </List>
  );
}
