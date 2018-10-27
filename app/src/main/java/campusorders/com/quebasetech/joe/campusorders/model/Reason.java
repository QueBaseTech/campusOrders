/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * Joe Nyugoh 27/ 10/ 2018.
 * MIT License
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package campusorders.com.quebasetech.joe.campusorders.model;

public class Reason {
    private String orderId, client, seller, reason, id;
    private Order.orderStatus orderStatus;
    private long cancelTime;

    public Reason() { }

    public Reason(String orderId, String client, String seller, String reason, String id, Order.orderStatus orderStatus, long cancelTime) {
        this.orderId = orderId;
        this.client = client;
        this.seller = seller;
        this.reason = reason;
        this.id = id;
        this.orderStatus = orderStatus;
        this.cancelTime = cancelTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order.orderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Order.orderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(long cancelTime) {
        this.cancelTime = cancelTime;
    }
}
