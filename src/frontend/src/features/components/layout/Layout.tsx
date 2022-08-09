import { Link, Outlet } from "react-router-dom";

export interface LayoutProps {}

export function Layout(props: LayoutProps) {
  return (
    <div>
      <nav>
        <ul>
          <li>
            <Link to="/">Users</Link>
          </li>
          <li>
            <Link to="/searchcode">Search all code</Link>
          </li>
        </ul>
      </nav>
      <Outlet />
    </div>
  );
}
