import {
  Card,
  CardContent,
  Typography,
  CardActions,
  Button,
} from "@mui/material";

export interface UserCardProps {
  name: string;
  url: string;
}

export function UserCard(props: UserCardProps) {
  return (
    <Card sx={{ minWidth: 275 }}>
      <CardContent>
        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
          {props.name}
        </Typography>
        <Typography sx={{ mb: 1.5 }} color="text.secondary">
          {props.url}
        </Typography>
        <Typography variant="body2">User details. See props.</Typography>
      </CardContent>
      <CardActions>
        <Button size="small">Search repos</Button>
      </CardActions>
    </Card>
  );
}
