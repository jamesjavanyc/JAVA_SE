package io.stream.Decorator;

public class TrueComponent extends Component {
    @Override
    public void read() {
        System.out.println("true conponent");
    }
}
