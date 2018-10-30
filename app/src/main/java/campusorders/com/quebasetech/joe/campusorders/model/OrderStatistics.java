/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * Joe Nyugoh 30/ 10/ 2018.
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

public class OrderStatistics {
    private String name;
    private int NEW, pending, delivered, rejected, cancelled, total;

    public OrderStatistics() {
    }

    public OrderStatistics(String name, int NEW, int pending, int delivered, int rejected, int cancelled, int total) {
        this.name = name;
        this.NEW = NEW;
        this.pending = pending;
        this.delivered = delivered;
        this.rejected = rejected;
        this.cancelled = cancelled;
        this.total = total;
    }

    public int getNEW() {
        return NEW;
    }

    public void setNEW(int NEW) {
        this.NEW = NEW;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    public int getRejected() {
        return rejected;
    }

    public void setRejected(int rejected) {
        this.rejected = rejected;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
