import {
  Card,
  CardContent,
  Typography,
  CardActions,
  Button,
} from "@mui/material";

export interface UserCardProps {}

export function UserCard(props: UserCardProps) {
  return (
    <Card sx={{ minWidth: 275 }}>
      <CardContent>
        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
          Username. (props)
        </Typography>
        <Typography sx={{ mb: 1.5 }} color="text.secondary">
          User details. See props.
        </Typography>
        <Typography variant="body2">User details. See props.</Typography>
      </CardContent>
      <CardActions>
        <Button size="small">Search repos</Button>
      </CardActions>
    </Card>
  );
}
