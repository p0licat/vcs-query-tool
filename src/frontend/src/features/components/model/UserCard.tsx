import {
  Card,
  CardContent,
  Typography,
  CardActions,
  Button,
} from "@mui/material";

export interface UserCardProps {
  username: string;
  fullname: string;
  url: string;
  redirectToSearchReposOfUser: Function;
}

export function UserCard(props: UserCardProps) {
  return (
    <Card sx={{ minWidth: 275 }}>
      <CardContent>
        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
          {props.fullname}
        </Typography>
        <Typography sx={{ mb: 1.5 }} color="text.secondary">
          {props.username}
        </Typography>
        <Typography variant="body2">{props.url}</Typography>
      </CardContent>
      <CardActions>
        <Button
          size="small"
          onClick={() => props.redirectToSearchReposOfUser(props.username)}
        >
          Search repos
        </Button>
      </CardActions>
    </Card>
  );
}
